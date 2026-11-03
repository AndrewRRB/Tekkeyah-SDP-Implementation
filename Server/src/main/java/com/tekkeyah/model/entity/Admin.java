package com.tekkeyah.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.tekkeyah.model.enums.Permission;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * PATTERN : INHERITANCE (the one real User subclass) - not a design pattern by
 *           itself, but it keeps OBSERVER + STRATEGY working unchanged
 * PART    : Admin, in the USERS box of the diagram
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   An Admin IS a User (solid arrow with a hollow triangle). Unlike Volunteer /
 *   Donor / Beneficiary it is a subclass, not an optional profile, because an
 *   admin is a different kind of account with its own permission set.
 *
 * HOW IT CONNECTS
 *   - Because it extends User it inherits update(...) from the OBSERVER side and
 *     the NotificationStrategy from the STRATEGY side - admins get notified the
 *     same way as everyone else with zero extra code.
 *   - Permission (enum) is shared with the DECORATOR pattern on the Volunteer
 *     branch, where each role layer also exposes a Set<Permission>.
 *   - Stored in its own table joined to "users" (InheritanceType.JOINED on User).
 * ============================================================================
 */
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "admin_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();

    public Admin() {}

    public Admin(String name, String email, String phone,
                 com.tekkeyah.pattern.strategy.NotificationStrategy strategy) {
        super(name, email, phone, strategy);
    }

    public boolean hasPermission(Permission p) {
        return permissions.contains(p);
    }

    /**
     * Gives permission p to user u. Only Admins hold permissions, so granting
     * to any other kind of User is rejected rather than silently ignored.
     */
    public void grantPermission(User u, Permission p) {
        if (!(u instanceof Admin target)) {
            throw new IllegalArgumentException("Only an Admin can hold permissions");
        }
        target.permissions.add(p);
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
