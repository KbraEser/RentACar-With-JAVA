package com.rentacar.controller;

import com.rentacar.dto.LoginRequest;
import com.rentacar.dto.LoginResponse;
import com.rentacar.dto.RegisterRequest;
import com.rentacar.dto.RegisterResponse;
import com.rentacar.entity.User;
import com.rentacar.repository.UserRepository;
import com.rentacar.service.AuthenticationService;
import com.rentacar.service.UserService;
import com.rentacar.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final UserService  userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authenticationManager,
            AuthenticationService authenticationService,
            UserService userService,
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public RegisterResponse registerResponse(@Valid @RequestBody RegisterRequest registerRequest) {
        User user =authenticationService.register(registerRequest);
        return new RegisterResponse(user.getEmail(),"Kayıt başarılı bir şekilde gerçekleşti.");
    }

    @PostMapping("/login")
    public LoginResponse loginResponse(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));


        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        String jwt = jwtUtil.generateToken(userDetails);

        return new LoginResponse(jwt,user.getId(), user.getEmail(), user.getName(),  user.getPassword());
    }
}
