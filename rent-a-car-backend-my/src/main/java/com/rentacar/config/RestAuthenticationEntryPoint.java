package com.rentacar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthErrorResponseWriter authErrorResponseWriter;

    public RestAuthenticationEntryPoint(AuthErrorResponseWriter authErrorResponseWriter) {
        this.authErrorResponseWriter = authErrorResponseWriter;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        authErrorResponseWriter.writeUnauthorized(response, "Oturum bulunamadı");
    }
}
