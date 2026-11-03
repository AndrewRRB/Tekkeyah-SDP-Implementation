package com.tekkeyah.model.entity;

/**
 * Permissions a user can hold. Volunteer roles (Decorator pattern) grant these,
 * and Admin uses them for access checks.
 */
public enum Permission {
    VIEW_EVENTS,
    DRIVE_VEHICLE,
    MANAGE_KITCHEN,
    MANAGE_DONATIONS
}
