package com.example.hexagonalapp.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String traceId = UUID.randomUUID().toString();
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        Map<String, Object> problemDetail = new HashMap<>();
        problemDetail.put("type", "https://example.com/validation-error");
        problemDetail.put("title", "Error de validación");
        problemDetail.put("status", status.value());
        problemDetail.put("detail", "Uno o más campos tienen errores");
        problemDetail.put("instance", path);
        problemDetail.put("timestamp", LocalDateTime.now());
        problemDetail.put("traceId", traceId);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        problemDetail.put("errors", errors);

        // Log estructurado
        logger.error("Validation error - TraceId: " + traceId +
                ", Path: " + path +
                ", Errors: " + errors);

        return new ResponseEntity<>(problemDetail, headers, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        String path = request.getRequestURI();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setType(URI.create("https://example.com/server-error"));
        problemDetail.setTitle("Error interno del servidor");
        problemDetail.setDetail("Ocurrió un error inesperado");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("traceId", traceId);
        problemDetail.setProperty("path", path);

        // Log estructurado
        logger.error(
                String.format(
                        "Internal error - TraceId: %s, Path: %s, Error: %s",
                        traceId, path, ex.getMessage()
                ),
                ex
        );

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}