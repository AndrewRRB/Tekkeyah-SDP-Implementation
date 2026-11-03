package com.tekkeyah.model.DTO;

import com.tekkeyah.model.entity.Admin;
import com.tekkeyah.model.entity.User;
import com.tekkeyah.model.enums.NotificationChannel;
import com.tekkeyah.model.enums.Permission;
import com.tekkeyah.model.enums.Skill;

import java.util.List;
import java.util.Set;


/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object)
 * PART    : What the server sends back about a user
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   A flat, safe snapshot of a User: basic details, the notification channel,
 *   which role profiles exist (the composition), and the role-specific data.
 *   Returning this instead of the entity avoids the User <-> Profile circular
 *   reference (infinite JSON) and hides internals.
 *
 * HOW IT CONNECTS
 *   Built by UserResponse.from(user) inside UserService; returned by every
 *   UserController endpoint.
 * ============================================================================
 */
public record UserResponse(
        int id,
        String name,
        String email,
        String phone,
        NotificationChannel channel,
        boolean admin,
        Set<Permission> permissions,
        boolean volunteer,
        Set<Skill> skills,
        double totalHours,
        boolean donor,
        double totalDonated,
        boolean beneficiary,
        List<String> needs) {

    public static UserResponse from(User u) {
        var v = u.getVolunteerProfile();
        var d = u.getDonorProfile();
        var b = u.getBeneficiaryProfile();
        Admin a = (u instanceof Admin admin) ? admin : null;
        return new UserResponse(
                u.getUserID(),
                u.getName(),
                u.getEmail(),
                u.getPhone(),
                u.getNotificationChannel(),
                a != null,
                a != null ? Set.copyOf(a.getPermissions()) : Set.of(),
                v != null,
                v != null ? Set.copyOf(v.getSkills()) : Set.of(),
                v != null ? v.getTotalHours() : 0,
                d != null,
                d != null ? d.getTotalDonated() : 0,
                b != null,
                b != null ? List.copyOf(b.getNeeds()) : List.of());
    }
}
