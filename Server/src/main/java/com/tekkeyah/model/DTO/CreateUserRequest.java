package com.tekkeyah.model.DTO;

import com.tekkeyah.model.enums.NotificationChannel;

/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object)
 * PART    : What the client sends to create a user
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   A plain carrier for the incoming JSON. We do not accept the User entity
 *   directly, so a client can never set fields it should not control
 *   (the id, the role profiles, permissions).
 *
 * HOW IT CONNECTS
 *   UserController receives it -> UserService.createUser(...) validates it and
 *   builds the real User. "channel" is optional; it defaults to EMAIL.
 * ============================================================================
 */
public record CreateUserRequest(String name, String email, String phone, NotificationChannel channel) {

}
