package com.tekkeyah.exception;

/**
 * Thrown by AuthService.login when the email / password pair does not match.
 * GlobalExceptionHandler turns it into an HTTP 401 response. The message is
 * deliberately the same for "unknown email" and "wrong password".
 */
public class InvalidCredentialsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
