package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Permission;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Decorator pattern — abstract Decorator.
 * Holds the wrapped role ({@code inner}) and delegates to it by default.
 * Concrete decorators extend the result with their own title, responsibilities and permissions.
 */
public abstract class VolunteerRoleDecorator implements VolunteerRole {

    protected final VolunteerRole inner;

    protected VolunteerRoleDecorator(VolunteerRole inner) {
        this.inner = Objects.requireNonNull(inner, "inner role must not be null");
    }

    @Override
    public String getTitle() {
        return inner.getTitle();
    }

    @Override
    public List<String> getResponsibilities() {
        return inner.getResponsibilities();
    }

    @Override
    public Set<Permission> getPermissions() {
        return inner.getPermissions();
    }

    /** Helper for subclasses: inner title plus this decorator's title. */
    protected String appendTitle(String title) {
        return inner.getTitle() + ", " + title;
    }

    /** Helper for subclasses: inner responsibilities plus this decorator's own. */
    protected List<String> appendResponsibilities(String... extra) {
        List<String> all = new ArrayList<>(inner.getResponsibilities());
        all.addAll(List.of(extra));
        return List.copyOf(all);
    }

    /** Helper for subclasses: inner permissions plus this decorator's own. */
    protected Set<Permission> appendPermissions(Permission first, Permission... rest) {
        Set<Permission> all = EnumSet.noneOf(Permission.class);
        all.addAll(inner.getPermissions());
        all.add(first);
        all.addAll(List.of(rest));
        return all;
    }
}
