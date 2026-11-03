package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Permission;

import java.util.List;
import java.util.Set;

/**
 * Decorator pattern — Concrete Decorator.
 * Adds kitchen duties and the MANAGE_KITCHEN permission.
 */
public class CookRole extends VolunteerRoleDecorator {

    public CookRole(VolunteerRole inner) {
        super(inner);
    }

    @Override
    public String getTitle() {
        return appendTitle("Cook");
    }

    @Override
    public List<String> getResponsibilities() {
        return appendResponsibilities(
                "Prepare meals for outreach events",
                "Keep the kitchen clean and food-safe");
    }

    @Override
    public Set<Permission> getPermissions() {
        return appendPermissions(Permission.MANAGE_KITCHEN);
    }
}
