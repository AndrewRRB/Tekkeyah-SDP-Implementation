package com.tekkeyah.pattern.decorator;

import com.tekkeyah.model.entity.Permission;
import com.tekkeyah.model.entity.Skill;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VolunteerRoleDecoratorTest {

    @Test
    void basicRoleOnlyViewsEvents() {
        VolunteerRole basic = new BasicVolunteerRole();

        assertEquals("Volunteer", basic.getTitle());
        assertEquals(EnumSet.of(Permission.VIEW_EVENTS), basic.getPermissions());
    }

    @Test
    void stackedDecoratorsAccumulateTitleResponsibilitiesAndPermissions() {
        VolunteerRole role = new CookRole(new DriverRole(new BasicVolunteerRole()));

        assertEquals("Volunteer, Driver, Cook", role.getTitle());
        assertEquals(EnumSet.of(Permission.VIEW_EVENTS, Permission.DRIVE_VEHICLE, Permission.MANAGE_KITCHEN),
                role.getPermissions());
        assertEquals(6, role.getResponsibilities().size());
    }

    @Test
    void decoratorsDoNotChangeTheWrappedRole() {
        VolunteerRole basic = new BasicVolunteerRole();
        new DriverRole(basic).getPermissions();

        assertEquals(EnumSet.of(Permission.VIEW_EVENTS), basic.getPermissions());
    }

    @Test
    void noRoleSkillsGiveTheBasicRole() {
        VolunteerRole role = VolunteerRoleType.roleFor(Set.of(Skill.TEACHING, Skill.FIRST_AID));

        assertEquals("Volunteer", role.getTitle());
        assertEquals(List.of(), VolunteerRoleType.decompose(role));
    }

    @Test
    void eachRoleSkillAddsItsDecorator() {
        VolunteerRole role = VolunteerRoleType.roleFor(Set.of(Skill.COOKING, Skill.DRIVING, Skill.TEACHING));

        assertEquals(List.of(VolunteerRoleType.DRIVER, VolunteerRoleType.COOK), VolunteerRoleType.decompose(role));
        assertEquals(EnumSet.of(Permission.VIEW_EVENTS, Permission.DRIVE_VEHICLE, Permission.MANAGE_KITCHEN),
                role.getPermissions());
    }

    @Test
    void removingASkillRemovesItsRole() {
        VolunteerRole before = VolunteerRoleType.roleFor(Set.of(Skill.COOKING, Skill.DRIVING));
        VolunteerRole after = VolunteerRoleType.roleFor(Set.of(Skill.COOKING));

        assertTrue(before.getPermissions().contains(Permission.DRIVE_VEHICLE));
        assertEquals("Volunteer, Cook", after.getTitle());
        assertEquals(EnumSet.of(Permission.VIEW_EVENTS, Permission.MANAGE_KITCHEN), after.getPermissions());
    }

    @Test
    void everyRoleTypeIsUnlockedByItsOwnSkill() {
        for (VolunteerRoleType type : VolunteerRoleType.values()) {
            VolunteerRole role = VolunteerRoleType.roleFor(Set.of(type.getRequiredSkill()));
            assertEquals(List.of(type), VolunteerRoleType.decompose(role));
        }
    }

    @Test
    void decomposeRejectsUnknownBaseRole() {
        VolunteerRole foreign = new VolunteerRole() {
            public String getTitle() { return "x"; }
            public List<String> getResponsibilities() { return List.of(); }
            public Set<Permission> getPermissions() { return EnumSet.noneOf(Permission.class); }
        };

        assertThrows(IllegalArgumentException.class,
                () -> VolunteerRoleType.decompose(new DriverRole(foreign)));
    }
}
