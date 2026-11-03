package com.tekkeyah.exception;

/**
 * ============================================================================
 * PATTERN : none (custom exception) - supports the error-handling layer
 * PART    : "that already exists" (e.g. email already registered)
 * ----------------------------------------------------------------------------
 * HOW IT CONNECTS
 *   Thrown by UserService.createUser when the email is taken (the users table
 *   also enforces this with a unique constraint as a second safety net).
 *   GlobalExceptionHandler turns it into an HTTP 409 response.
 * ============================================================================
 */
public class DuplicateResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) {
        super(message);
    }
}
