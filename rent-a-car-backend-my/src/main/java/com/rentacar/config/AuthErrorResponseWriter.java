package com.rentacar.config;

import com.rentacar.exceptions.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthErrorResponseWriter {

    private final JsonMapper jsonMapper;

    public AuthErrorResponseWriter(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionResponse body = new ExceptionResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                message,
                LocalDateTime.now()
        );

        response.getWriter().write(jsonMapper.writeValueAsString(body));
    }
}
