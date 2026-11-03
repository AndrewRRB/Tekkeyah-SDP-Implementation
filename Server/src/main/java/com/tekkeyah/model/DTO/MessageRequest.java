package com.tekkeyah.model.DTO;

/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object) - used to DEMO Observer/Strategy output
 * PART    : A message to push to one user through their chosen channel
 * ----------------------------------------------------------------------------
 * HOW IT CONNECTS
 *   POST /api/users/{id}/notify -> UserService.notifyUser(...) -> User.notify(),
 *   which delivers through whichever NotificationStrategy the user has. It is
 *   the same path an Event / Donation / Payment takes later, when it calls
 *   notifyObservers(...) and Java invokes User.update(...).
 * ============================================================================
 */
public record MessageRequest(String message) {
}
