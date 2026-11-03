package com.tekkeyah.model.enums;

/**
 * The role a person picks when signing up. Each value maps to one optional
 * role profile on User (composition), not to a subclass.
 */
public enum AccountRole {
    DONOR,
    VOLUNTEER,
    BENEFICIARY
}
