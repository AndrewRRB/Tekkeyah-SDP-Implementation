package com.tekkeyah.model.DTO;

import com.tekkeyah.model.enums.AccountRole;
import com.tekkeyah.model.enums.NotificationChannel;

/**
 * POST /api/auth/signup body. "phone", "channel" and "role" are optional
 * (channel defaults to EMAIL, role defaults to DONOR).
 */
public record SignUpRequest(String fullName,
                            String email,
                            String password,
                            String phone,
                            NotificationChannel channel,
                            AccountRole role) {
}
