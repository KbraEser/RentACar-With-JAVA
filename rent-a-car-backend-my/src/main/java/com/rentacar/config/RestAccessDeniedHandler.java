package com.rentacar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final AuthErrorResponseWriter authErrorResponseWriter;

    public RestAccessDeniedHandler(AuthErrorResponseWriter authErrorResponseWriter) {
        this.authErrorResponseWriter = authErrorResponseWriter;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            authErrorResponseWriter.writeUnauthorized(response, "Oturum bulunamadı");
            return;
        }

        authErrorResponseWriter.writeForbidden(response, "Bu işlem için yetkiniz yok");
    }
}
