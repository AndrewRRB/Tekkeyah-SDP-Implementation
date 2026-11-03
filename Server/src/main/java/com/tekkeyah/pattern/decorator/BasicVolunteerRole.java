package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Permission;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Decorator pattern — Concrete Component.
 * The base role every volunteer starts with.
 */
public class BasicVolunteerRole implements VolunteerRole {

    @Override
    public String getTitle() {
        return "Volunteer";
    }

    @Override
    public List<String> getResponsibilities() {
        return List.of(
                "Attend assigned events",
                "Follow the event coordinator's instructions");
    }

    @Override
    public Set<Permission> getPermissions() {
        return EnumSet.of(Permission.VIEW_EVENTS);
    }
}
