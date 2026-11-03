package com.tekkeyah.model.DTO;

import com.tekkeyah.model.enums.NotificationChannel;

/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object) - drives the STRATEGY swap
 * PART    : What the client sends to change how a user is notified
 * ----------------------------------------------------------------------------
 * HOW IT CONNECTS
 *   PUT /api/users/{id}/channel -> UserService.changeChannel(...), which swaps
 *   the user's NotificationStrategy at runtime (the Strategy pattern's whole
 *   point) and saves the new channel name.
 * ============================================================================
 */
public record ChannelRequest(NotificationChannel channel) {
}
