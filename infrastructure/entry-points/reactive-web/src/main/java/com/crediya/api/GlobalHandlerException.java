package com.crediya.api;

import com.crediya.usecase.exception.ArgumentException;
import com.crediya.usecase.exception.DuplicateEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

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

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(DuplicateEmailException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "DUPLICATE_EMAIL",
                Arrays.asList(ex.getMessage())
        );
    }

    @ExceptionHandler(ArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ArgumentException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                Arrays.asList(ex.getMessage())
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