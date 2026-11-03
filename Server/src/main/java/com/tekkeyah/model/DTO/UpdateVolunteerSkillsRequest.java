package com.tekkeyah.model.DTO;

import com.tekkeyah.model.entity.Skill;

import java.util.Set;

/** PUT /api/volunteers/{id}/skills body — the full new set of skills. */
public record UpdateVolunteerSkillsRequest(Set<Skill> skills) {
}
