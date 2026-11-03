package com.tekkeyah.exception;

/**
 * ============================================================================
 * PATTERN : none (custom exception) - supports the error-handling layer
 * PART    : "the thing you asked for does not exist"
 * ----------------------------------------------------------------------------
 * HOW IT CONNECTS
 *   Thrown by UserService when an id is unknown.
 *   GlobalExceptionHandler turns it into an HTTP 404 response.
 * ============================================================================
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
