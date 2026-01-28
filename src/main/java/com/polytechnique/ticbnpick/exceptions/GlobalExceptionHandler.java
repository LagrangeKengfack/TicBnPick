package com.polytechnique.ticbnpick.exceptions;

import com.polytechnique.ticbnpick.exceptions.address.AddressOperationException;
import com.polytechnique.ticbnpick.exceptions.address.InvalidAddressException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 * Handles common exceptions and returns appropriate error responses.
 *
 * @author Kenmeugne Michèle
 * @date 18/12/2025
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     * Returns 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    /**
     * Handles DuplicateResourceException.
     * Returns 409 Conflict.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDuplicateResourceException(
            DuplicateResourceException ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    /**
     * Handles InvalidAddressException.
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(InvalidAddressException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidAddressException(
            InvalidAddressException ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    /**
     * Handles AddressOperationException.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(AddressOperationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAddressOperationException(
            AddressOperationException ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

    /**
     * Handles duplicate key exceptions from database.
     * Returns 409 Conflict.
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDuplicateKeyException(
            DuplicateKeyException ex,
            ServerWebExchange exchange) {

        String message = "A resource with this information already exists";

        if (ex.getMessage().contains("persons_email_key")) {
            message = "Email already exists";
        } else if (ex.getMessage().contains("persons_national_id_key")) {
            message = "National ID already exists";
        } else if (ex.getMessage().contains("addresses_street_city_district_country_key")) {
            message = "Address with same street, city, district and country already exists";
        }

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                message,
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    /**
     * Handles validation errors.
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(
            WebExchangeBindException ex,
            ServerWebExchange exchange) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Validation error");

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    /**
     * Handles IllegalArgumentException.
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    /**
     * Handles InvalidCredentialsException.
     * Returns 401 Unauthorized.
     *
     * @param ex exchange
     * @param exchange web exchange
     * @return error response
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
    }

    /**
     * Handles all other exceptions.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                exchange.getRequest().getPath().value()
        );

        logError(ex, exchange);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

    /**
     * Log error details for debugging.
     */
    private void logError(Exception ex, ServerWebExchange exchange) {
        System.err.printf("[%s] %s %s - %s: %s%n",
                LocalDateTime.now(),
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath().value(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }
}