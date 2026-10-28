
package com.scaler.bookMyShow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        String message = exception.getMessage();

        HttpStatus status = "Email is already registered".equals(message)
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;

        Map<String, String> response = Map.of(
                "error", message == null ? "Invalid request" : message
        );

        return ResponseEntity.status(status).body(response);
    }
}
