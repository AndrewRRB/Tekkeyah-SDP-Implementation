package com.tekkeyah.service;

import com.tekkeyah.exception.DuplicateResourceException;
import com.tekkeyah.exception.ResourceNotFoundException;
import com.tekkeyah.model.DTO.UserResponse;
import com.tekkeyah.model.enums.NotificationChannel;
import com.tekkeyah.model.enums.Skill;
import com.tekkeyah.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void createsAndReadsUserFromDatabase() {
        UserResponse created = userService.createUser(
                "  Samira Hassan  ", "  Samira@Example.com  ", "01012345678", null);

        assertTrue(created.id() > 0);
        assertEquals("Samira Hassan", created.name());
        assertEquals("samira@example.com", created.email());
        assertEquals(NotificationChannel.EMAIL, created.channel());
        assertEquals(1, userRepository.count());
        assertEquals(created, userService.getUser(created.id()));
        assertEquals(List.of(created), userService.getAllUsers());
    }

    @Test
    void rejectsInvalidOrDuplicateUsers() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(" ", "valid@example.com", null, null));
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser("Valid", "not-an-email", null, null));

        userService.createUser("Valid", "valid@example.com", null, null);

        assertThrows(DuplicateResourceException.class,
                () -> userService.createUser("Another", " VALID@example.com ", null, null));
        assertEquals(1, userRepository.count());
    }

    @Test
    void changesNotificationChannelAndCanNotifyUser() {
        UserResponse created = userService.createUser(
                "Nour", "nour@example.com", "01000000000", NotificationChannel.EMAIL);

        UserResponse changed = userService.changeChannel(created.id(), NotificationChannel.WHATSAPP);

        assertEquals(NotificationChannel.WHATSAPP, changed.channel());
        assertEquals(NotificationChannel.WHATSAPP, userService.getUser(created.id()).channel());
        userService.notifyUser(created.id(), "Test notification");

        assertThrows(IllegalArgumentException.class,
                () -> userService.changeChannel(created.id(), null));
        assertThrows(IllegalArgumentException.class,
                () -> userService.notifyUser(created.id(), " "));
    }

    @Test
    void persistsRoleProfilesSkillsAndNeeds() {
        UserResponse created = userService.createUser(
                "Mariam", "mariam@example.com", null, NotificationChannel.SMS);

        UserResponse volunteer = userService.becomeVolunteer(created.id());
        assertTrue(volunteer.volunteer());
        assertTrue(userService.becomeVolunteer(created.id()).volunteer());

        UserResponse withSkills = userService.updateSkills(
                created.id(), Set.of(Skill.COOKING, Skill.DRIVING));
        assertEquals(Set.of(Skill.COOKING, Skill.DRIVING), withSkills.skills());

        UserResponse donor = userService.becomeDonor(created.id());
        assertTrue(donor.donor());

        UserResponse beneficiary = userService.becomeBeneficiary(created.id());
        assertTrue(beneficiary.beneficiary());

        UserResponse withNeed = userService.addNeed(created.id(), "  Winter clothes  ");
        assertEquals(List.of("Winter clothes"), withNeed.needs());

        UserResponse loaded = userService.getUser(created.id());
        assertTrue(loaded.volunteer());
        assertEquals(Set.of(Skill.COOKING, Skill.DRIVING), loaded.skills());
        assertTrue(loaded.donor());
        assertTrue(loaded.beneficiary());
        assertEquals(List.of("Winter clothes"), loaded.needs());
    }

    @Test
    void requiresMatchingProfilesBeforeUpdatingTheirDetails() {
        UserResponse created = userService.createUser(
                "Hana", "hana@example.com", null, null);

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateSkills(created.id(), Set.of(Skill.COOKING)));
        assertThrows(IllegalArgumentException.class,
                () -> userService.addNeed(created.id(), "Food"));

        userService.becomeBeneficiary(created.id());
        assertThrows(IllegalArgumentException.class,
                () -> userService.addNeed(created.id(), " "));
    }

    @Test
    void deletesUserAndItsComposedProfiles() {
        UserResponse created = userService.createUser(
                "Rana", "rana@example.com", null, null);
        userService.becomeVolunteer(created.id());
        userService.becomeDonor(created.id());
        userService.becomeBeneficiary(created.id());
        userRepository.flush();

        userService.deleteUser(created.id());
        userRepository.flush();

        assertEquals(0, userRepository.count());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(created.id()));
    }

    @Test
    void reportsMissingUserForUserOperations() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(999));
        assertThrows(ResourceNotFoundException.class,
                () -> userService.changeChannel(999, NotificationChannel.SMS));
        assertThrows(ResourceNotFoundException.class,
                () -> userService.becomeVolunteer(999));
        assertFalse(userService.getAllUsers().stream().findAny().isPresent());
    }
}
