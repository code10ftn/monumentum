package io.github.code10.monumentumservice.controller.exception;


import io.github.code10.monumentumservice.controller.exception.resolver.ExceptionResolver;

/**
 * Custom exception.
 * Gets mapped to {@link org.springframework.http.HttpStatus#BAD_REQUEST} when caught in
 * {@link ExceptionResolver}.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
