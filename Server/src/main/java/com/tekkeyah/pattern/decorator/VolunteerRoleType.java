package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * The concrete decorators that can be stacked on a {@link BasicVolunteerRole},
 * each unlocked by one {@link Skill}. A volunteer's role is never assigned by hand:
 * it is rebuilt from the volunteer's skills with {@link #roleFor(Set)}.
 */
public enum VolunteerRoleType {

    DRIVER(DriverRole.class, DriverRole::new, Skill.DRIVING),
    COOK(CookRole.class, CookRole::new, Skill.COOKING);

    private final Class<? extends VolunteerRoleDecorator> decoratorClass;
    private final UnaryOperator<VolunteerRole> wrapper;
    private final Skill requiredSkill;

    VolunteerRoleType(Class<? extends VolunteerRoleDecorator> decoratorClass,
                      UnaryOperator<VolunteerRole> wrapper,
                      Skill requiredSkill) {
        this.decoratorClass = decoratorClass;
        this.wrapper = wrapper;
        this.requiredSkill = requiredSkill;
    }

    public Skill getRequiredSkill() {
        return requiredSkill;
    }

    /** Wraps {@code role} with this type's decorator. */
    public VolunteerRole decorate(VolunteerRole role) {
        return wrapper.apply(role);
    }

    /**
     * Builds the decorator chain a volunteer with these skills gets:
     * BasicVolunteerRole wrapped by every role whose required skill is present,
     * in declaration order (so the result is deterministic).
     */
    public static VolunteerRole roleFor(Set<Skill> skills) {
        VolunteerRole role = new BasicVolunteerRole();
        for (VolunteerRoleType type : values()) {
            if (skills.contains(type.requiredSkill)) {
                role = type.decorate(role);
            }
        }
        return role;
    }

    /** Reads a decorator chain back as types, innermost first (used for API responses). */
    public static List<VolunteerRoleType> decompose(VolunteerRole role) {
        List<VolunteerRoleType> types = new ArrayList<>();
        VolunteerRole current = role;
        while (current instanceof VolunteerRoleDecorator decorator) {
            types.add(of(decorator));
            current = decorator.inner;
        }
        if (!(current instanceof BasicVolunteerRole)) {
            throw new IllegalArgumentException(
                    "Role chain must end in BasicVolunteerRole but ended in " + current);
        }
        Collections.reverse(types);
        return List.copyOf(types);
    }

    private static VolunteerRoleType of(VolunteerRoleDecorator decorator) {
        for (VolunteerRoleType type : values()) {
            if (type.decoratorClass == decorator.getClass()) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "No VolunteerRoleType registered for " + decorator.getClass().getName());
    }
}
