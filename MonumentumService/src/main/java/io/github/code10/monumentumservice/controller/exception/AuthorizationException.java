package io.github.code10.monumentumservice.controller.exception;


import io.github.code10.monumentumservice.controller.exception.resolver.ExceptionResolver;

/**
 * Custom exception.
 * Gets mapped to {@link org.springframework.http.HttpStatus#UNAUTHORIZED} when caught in
 * {@link ExceptionResolver}.
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
