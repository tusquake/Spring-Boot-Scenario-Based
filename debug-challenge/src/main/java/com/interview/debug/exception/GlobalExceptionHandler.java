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
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied: You do not have permission to perform this action.");
    }

    // 1. Handle Specific Exceptions (e.g., 400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 1.5 Handle Validation Errors (e.g., @NotBlank, @Size)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, String> errors = new java.util.HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = (error instanceof org.springframework.validation.FieldError)
                    ? ((org.springframework.validation.FieldError) error).getField()
                    : error.getObjectName();
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

    // 1.6 Handle Optimistic Locking Failures (409 Conflict)
    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailure(
            org.springframework.orm.ObjectOptimisticLockingFailureException ex) {
        return buildResponse(HttpStatus.CONFLICT,
                "The record you are trying to update has been modified by another user. Please refresh and try again.");
    }

    // 1.7 Handle Pessimistic Locking Failures / timeouts (423 Locked)
    @ExceptionHandler(org.springframework.dao.PessimisticLockingFailureException.class)
    public ResponseEntity<Object> handlePessimisticLockingFailure(
            org.springframework.dao.PessimisticLockingFailureException ex) {
        return buildResponse(HttpStatus.LOCKED,
                "The resource is currently locked by another process and the request timed out. Please try again in a moment.");
    }

    @ExceptionHandler(BalanceCannotbeNegativeException.class)
    public ResponseEntity<Object> BalanceCannotbeNegativeException(BalanceCannotbeNegativeException ex) {
        return buildResponse(HttpStatus.PAYMENT_REQUIRED,
                "The Balance cannot be negative.");
    }

    // 2. Handle Resource Not Found (e.g., 404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 2.5 Handle Built-in ResponseStatusException (to keep format consistent)
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(
            org.springframework.web.server.ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        return buildResponse(status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR, ex.getReason());
    }

    // 3. Global Fallback (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred: " + ex.getMessage());
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
