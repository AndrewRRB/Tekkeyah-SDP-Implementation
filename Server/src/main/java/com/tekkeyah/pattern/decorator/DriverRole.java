package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Permission;

import java.util.List;
import java.util.Set;

/**
 * Decorator pattern — Concrete Decorator.
 * Adds driving duties and the DRIVE_VEHICLE permission.
 */
public class DriverRole extends VolunteerRoleDecorator {

    public DriverRole(VolunteerRole inner) {
        super(inner);
    }

    @Override
    public String getTitle() {
        return appendTitle("Driver");
    }

    @Override
    public List<String> getResponsibilities() {
        return appendResponsibilities(
                "Transport donations and supplies",
                "Drive volunteers and beneficiaries to outreach sites");
    }

    @Override
    public Set<Permission> getPermissions() {
        return appendPermissions(Permission.DRIVE_VEHICLE);
    }
}
