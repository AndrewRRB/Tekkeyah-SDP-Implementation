package com.tekkeyah.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * ============================================================================
 * PATTERN : none (central error handling, Spring "controller advice")
 * PART    : One place that converts exceptions into clean HTTP responses
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   Without this, a bad request would show a stack trace. With it, every
 *   controller returns the same JSON shape: {"error": "..."}.
 *
 * HOW IT CONNECTS
 *   - ResourceNotFoundException      -> 404
 *   - DuplicateResourceException     -> 409
 *   - InvalidCredentialsException    -> 401 (AuthService.login)
 *   - HttpMessageNotReadableException -> 400 (bad JSON / unknown enum value)
 *   - DataIntegrityViolationException -> 409 (a DB constraint refused the change)
 *   - IllegalArgumentException       -> 400 (bad input; thrown by UserService
 *                                      and by the model, e.g. Admin.grantPermission)
 *   - Applies to every controller automatically (UserController and the
 *     controllers other branches add later).
 * ============================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(ResourceNotFoundException e) {
        return body(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> conflict(DuplicateResourceException e) {
        return body(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> unauthorized(InvalidCredentialsException e) {
        return body(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
        return body(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> unreadable(HttpMessageNotReadableException e) {
        return body(HttpStatus.BAD_REQUEST, "malformed request body");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> constraint(DataIntegrityViolationException e) {
        return body(HttpStatus.CONFLICT, "the change conflicts with existing data");
    }

    private ResponseEntity<Map<String, String>> body(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(message)));
    }
}
