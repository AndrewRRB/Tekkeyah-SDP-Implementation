package com.tekkeyah.model.DTO;

/** Result of a skills update: the volunteer with the rebuilt role, and how many open tasks they were removed from. */
public record UpdateVolunteerSkillsResponse(VolunteerResponse volunteer, int unassignedTaskCount) {
}
