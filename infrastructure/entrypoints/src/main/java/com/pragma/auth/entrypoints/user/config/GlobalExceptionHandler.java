package com.pragma.auth.entrypoints.user.config;

import com.pragma.auth.usecase.user.UserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String errorCode, List<String> errorMessages) {

        Map<String, Object> response = Map.of(
                "error", errorCode,
                "errors", errorMessages
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(IllegalArgumentException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                Arrays.asList(ex.getMessage())
        );
    }

    @ExceptionHandler(UserUseCase.DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(UserUseCase.DuplicateEmailException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "DUPLICATE_EMAIL",
                Arrays.asList(ex.getMessage())
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(WebExchangeBindException ex) {
        List<String> errorMessages = ex.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                errorMessages
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                Arrays.asList("Internal server error")
        );
    }
}