package com.tekkeyah.model.DTO;

import com.tekkeyah.model.entity.Skill;
import com.tekkeyah.model.entity.Volunteer;
import com.tekkeyah.pattern.decorator.VolunteerRoleType;

import java.util.List;
import java.util.Set;

/** Volunteer as returned by the API. Roles live on shifts; this shows which roles the skills can unlock. */
public record VolunteerResponse(
        Integer id,
        String name,
        String email,
        String phone,
        Set<Skill> skills,
        List<VolunteerRoleType> possibleRoles,
        double totalHours) {

    public static VolunteerResponse from(Volunteer v) {
        return new VolunteerResponse(
                v.getUserID(),
                v.getName(),
                v.getEmail(),
                v.getPhone(),
                Set.copyOf(v.getSkills()),
                v.getPossibleRoles(),
                v.getTotalHours());
    }
}
