package com.tekkeyah.model.DTO;

import com.tekkeyah.model.enums.NotificationChannel;

/**
 * What sign-up / login return: the user's public details plus a display role.
 * Never contains the password hash.
 */
public record AuthResponse(int id,
                           String name,
                           String email,
                           String phone,
                           NotificationChannel channel,
                           String role,
                           boolean admin,
                           boolean donor,
                           boolean volunteer,
                           boolean beneficiary,
                           double totalDonated) {

    public static AuthResponse from(UserResponse u) {
        return new AuthResponse(u.id(), u.name(), u.email(), u.phone(), u.channel(),
                displayRole(u), u.admin(), u.donor(), u.volunteer(), u.beneficiary(),
                u.totalDonated());
    }

    private static String displayRole(UserResponse u) {
        if (u.admin()) return "Admin";
        if (u.donor()) return "Donor";
        if (u.volunteer()) return "Volunteer";
        if (u.beneficiary()) return "Beneficiary";
        return "Member";
    }
}
