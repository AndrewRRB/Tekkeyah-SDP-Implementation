package com.tekkeyah.model.DTO;

import com.tekkeyah.model.entity.Permission;
import com.tekkeyah.model.entity.VolunteerShift;
import com.tekkeyah.pattern.decorator.VolunteerRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/** A shift with the role (Decorator chain) it grants. */
public record VolunteerShiftResponse(
        Integer shiftId,
        LocalDateTime start,
        LocalDateTime end,
        boolean active,
        String roleTitle,
        List<String> responsibilities,
        Set<Permission> permissions) {

    public static VolunteerShiftResponse from(VolunteerShift s, LocalDateTime now) {
        VolunteerRole role = s.getRole();
        return new VolunteerShiftResponse(
                s.getShiftID(),
                s.getStart(),
                s.getEnd(),
                s.isActive(now),
                role.getTitle(),
                role.getResponsibilities(),
                role.getPermissions());
    }
}
