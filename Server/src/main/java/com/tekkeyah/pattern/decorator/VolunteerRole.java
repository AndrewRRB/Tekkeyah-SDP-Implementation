package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Permission;

import java.util.List;
import java.util.Set;

/**
 * Decorator pattern — Component.
 * A volunteer's role; extra responsibilities and permissions are attached at runtime
 * by wrapping a {@link BasicVolunteerRole} in {@link VolunteerRoleDecorator}s.
 */
public interface VolunteerRole {

    String getTitle();

    List<String> getResponsibilities();

    Set<Permission> getPermissions();
}
