package io.github.code10.monumentumservice.controller.exception;

import io.github.code10.monumentumservice.controller.exception.resolver.ExceptionResolver;

/**
 * Custom exception.
 * Gets mapped to {@link org.springframework.http.HttpStatus#NOT_FOUND} when caught in
 * {@link ExceptionResolver}.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
