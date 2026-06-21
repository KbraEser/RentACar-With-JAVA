package com.rentacar.config;

import com.rentacar.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthCookieService authCookieService;
    private final AuthErrorResponseWriter authErrorResponseWriter;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            AuthCookieService authCookieService,
            AuthErrorResponseWriter authErrorResponseWriter
    ) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authCookieService = authCookieService;
        this.authErrorResponseWriter = authErrorResponseWriter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = resolveToken(request);

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username;

            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                authErrorResponseWriter.writeUnauthorized(response, "Geçersiz veya süresi dolmuş token");
                return;
            }

            if (jwtUtil.isRefreshToken(jwtToken)) {
                authErrorResponseWriter.writeUnauthorized(response, "Erişim token'ı gerekli");
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtUtil.validateToken(jwtToken, userDetails)) {
                authErrorResponseWriter.writeUnauthorized(response, "Geçersiz veya süresi dolmuş token");
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String cookieToken = authCookieService.resolveAccessToken(request);
        if (cookieToken != null) {
            return cookieToken;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }
}
