package com.tekkeyah.model.DTO;

import com.tekkeyah.model.entity.Skill;

import java.util.Set;

/** POST /api/volunteers body. The volunteer's role is derived from the skills. */
public record CreateVolunteerRequest(
        String name,
        String email,
        String phone,
        Set<Skill> skills) {
}
