package com.interview.debug.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Specific Exceptions (e.g., 400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 1.5 Handle Validation Errors (e.g., @NotBlank, @Size)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, String> errors = new java.util.HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = (error instanceof org.springframework.validation.FieldError) ? 
                               ((org.springframework.validation.FieldError) error).getField() : 
                               error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("details", errors);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 2. Handle Resource Not Found (e.g., 404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 2.5 Handle Built-in ResponseStatusException (to keep format consistent)
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(org.springframework.web.server.ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        return buildResponse(status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR, ex.getReason());
    }

    // 3. Global Fallback (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        ex.printStackTrace(); // Log to console
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred.");
    }

    // Helper method to keep the code DRY (Don't Repeat Yourself)
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        
        return new ResponseEntity<>(body, status);
    }
}
