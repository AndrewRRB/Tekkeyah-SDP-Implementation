package com.tekkeyah.model.DTO;

import com.tekkeyah.model.enums.Skill;

import java.util.Set;

/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object) - feeds the DECORATOR pattern (later)
 * PART    : What a volunteer sends to declare their skills
 * ----------------------------------------------------------------------------
 * HOW IT CONNECTS
 *   PUT /api/users/{id}/volunteer/skills -> UserService.updateSkills(...) ->
 *   VolunteerProfile.updateSkills(...). On the Volunteer branch the same call
 *   will rebuild the volunteer's role decorator chain from these skills.
 * ============================================================================
 */
public record SkillsRequest(Set<Skill> skills) {
}
