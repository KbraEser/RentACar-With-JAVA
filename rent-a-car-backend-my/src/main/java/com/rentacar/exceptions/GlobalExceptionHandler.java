package com.rentacar.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    @Autowired
    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleApiException(ApiException exception) {
        log.error("ApiException: {} | status: {}",exception.getMessage(),exception.getHttpStatus());

        ExceptionResponse response = new ExceptionResponse(
                exception.getHttpStatus().value(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Geçersiz istek");

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception exception) {
        log.error("Beklenmeyen hata", exception);

        String clientMessage = isProductionProfile()
                ? "Beklenmeyen bir hata oluştu"
                : exception.getMessage();

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                clientMessage,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isProductionProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase("prod"));
    }
}
