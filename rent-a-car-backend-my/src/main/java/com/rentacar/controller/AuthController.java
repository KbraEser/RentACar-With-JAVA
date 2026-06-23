package com.rentacar.controller;

import com.rentacar.config.AuthCookieService;
import com.rentacar.dto.LoginRequest;
import com.rentacar.dto.RegisterRequest;
import com.rentacar.dto.RegisterResponse;
import com.rentacar.dto.UserProfileResponse;
import com.rentacar.entity.User;
import com.rentacar.exceptions.ApiException;
import com.rentacar.repository.UserRepository;
import com.rentacar.redis.LoginRateLimiter;
import com.rentacar.service.AuthenticationService;
import com.rentacar.service.UserService;
import com.rentacar.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthCookieService authCookieService;
    private final LoginRateLimiter loginRateLimiter;

    public AuthController(
            AuthenticationManager authenticationManager,
            AuthenticationService authenticationService,
            UserService userService,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            AuthCookieService authCookieService,
            LoginRateLimiter loginRateLimiter
    ) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authCookieService = authCookieService;
        this.loginRateLimiter = loginRateLimiter;
    }

    @PostMapping("/register")
    public RegisterResponse registerResponse(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authenticationService.register(registerRequest);
        return new RegisterResponse(user.getEmail(), "Kayıt başarılı bir şekilde gerçekleşti.");
    }

    @PostMapping("/login")
    public UserProfileResponse loginResponse(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        if (loginRateLimiter.isBlocked(loginRequest.getEmail())) {
            throw new ApiException(
                    "Çok fazla başarısız giriş denemesi. Lütfen daha sonra tekrar deneyin.",
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            loginRateLimiter.recordFailedAttempt(loginRequest.getEmail());
            throw new ApiException("Geçersiz email veya şifre", HttpStatus.UNAUTHORIZED);
        }

        loginRateLimiter.clearAttempts(loginRequest.getEmail());

        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        authCookieService.setAuthCookies(response, accessToken, refreshToken);

        return toUserProfile(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authCookieService.resolveRefreshToken(request);

        if (refreshToken == null) {
            throw new ApiException("Oturum bulunamadı", HttpStatus.UNAUTHORIZED);
        }

        String username;
        try {
            username = jwtUtil.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new ApiException("Geçersiz oturum", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (!jwtUtil.validateRefreshToken(refreshToken, userDetails)) {
            throw new ApiException("Oturum süresi dolmuş", HttpStatus.UNAUTHORIZED);
        }

        String newAccessToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
        authCookieService.setAuthCookies(response, newAccessToken, newRefreshToken);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authCookieService.clearAuthCookies(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public UserProfileResponse currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ApiException("Oturum bulunamadı", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ApiException("Kullanıcı bulunamadı", HttpStatus.UNAUTHORIZED));

        return toUserProfile(user);
    }

    private UserProfileResponse toUserProfile(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname()
        );
    }
}
