package com.eventmanagement.infrastructure.exception;

import com.eventmanagement.domain.exception.DomainException;
import com.eventmanagement.infrastructure.adapters.in.web.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String traceId = UUID.randomUUID().toString();
        log.error("Validation error - Trace ID: {}", traceId, ex);

        return ResponseEntity.badRequest()
                .body(ApiResponse.<Map<String, String>>error("Validation failed", traceId));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Constraint violation - Trace ID: {}", traceId, ex);

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Constraint violation: " + ex.getMessage(), traceId));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityNotFoundException(
            EntityNotFoundException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Entity not found - Trace ID: {}", traceId, ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Resource not found: " + ex.getMessage(), traceId));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<String>> handleDomainException(DomainException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Domain exception - Trace ID: {}", traceId, ex);

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage(), traceId));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Data integrity violation - Trace ID: {}", traceId, ex);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Data integrity violation: " + ex.getMessage(), traceId));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(
            BadCredentialsException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Authentication failed - Trace ID: {}", traceId, ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid username or password", traceId));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(
            AccessDeniedException ex) {
        String traceId = UUID.randomUUID().toString();
        log.error("Access denied - Trace ID: {}", traceId, ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Access denied: " + ex.getMessage(), traceId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex, WebRequest request) {
        String traceId = UUID.randomUUID().toString();
        log.error("Internal server error - Trace ID: {} - Path: {}",
                traceId, request.getDescription(false), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred", traceId));
    }
}
