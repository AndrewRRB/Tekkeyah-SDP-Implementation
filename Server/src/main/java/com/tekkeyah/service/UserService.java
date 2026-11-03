package com.tekkeyah.service;

import com.tekkeyah.exception.DuplicateResourceException;
import com.tekkeyah.exception.ResourceNotFoundException;
import com.tekkeyah.model.DTO.UserResponse;
import com.tekkeyah.model.entity.User;
import com.tekkeyah.model.enums.AccountRole;
import com.tekkeyah.model.enums.NotificationChannel;
import com.tekkeyah.model.enums.Skill;
import com.tekkeyah.pattern.strategy.EmailNotification;
import com.tekkeyah.pattern.strategy.NotificationStrategy;
import com.tekkeyah.pattern.strategy.SMSNotification;
import com.tekkeyah.pattern.strategy.SocialMediaNotification;
import com.tekkeyah.pattern.strategy.WhatsAppNotification;
import com.tekkeyah.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * ============================================================================
 * PATTERNS : STRATEGY (picks and restores the strategy object)
 *            COMPOSITION (creates role profiles through the User)
 *            OBSERVER (the notify entry point that Subjects also use)
 * PART     : Business logic for Users - the layer between controller and DB
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   All the rules live here: validate input, keep emails unique, save, and
 *   keep every User's live NotificationStrategy in sync with the channel name
 *   stored in the database.
 *
 * THE KEY JOB (Strategy restore)
 *   The database stores only the channel NAME (EMAIL, SMS, ...). Whenever a
 *   User is created, loaded, or has its channel changed, hydrate() turns the
 *   name into the matching strategy object and calls user.setStrategy(...).
 *   This is the only class that knows which channel maps to which strategy,
 *   so adding a new channel later means editing strategyFor() and nothing else.
 *
 * HOW IT CONNECTS
 *   - UserController calls it; it returns UserResponse DTOs, never entities.
 *   - UserRepository saves and loads users (profiles ride along via cascade).
 *   - The four strategy classes are injected by Spring and used as the
 *     interchangeable algorithms.
 *   - becomeVolunteer / becomeDonor / becomeBeneficiary on User do the
 *     composition; this class just saves the result.
 *   - Event / Donation / Payment (other branches) reach users through
 *     User.update(), which uses the strategy set here.
 * ============================================================================
 */
@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final EmailNotification email;
    private final SMSNotification sms;
    private final WhatsAppNotification whatsApp;
    private final SocialMediaNotification socialMedia;

    public UserService(UserRepository repository,
                       EmailNotification email,
                       SMSNotification sms,
                       WhatsAppNotification whatsApp,
                       SocialMediaNotification socialMedia) {
        this.repository = repository;
        this.email = email;
        this.sms = sms;
        this.whatsApp = whatsApp;
        this.socialMedia = socialMedia;
    }

    // ------------------------------ USERS ----------------------------------

    public UserResponse createUser(String name, String emailAddress, String phone,
                                   NotificationChannel channel) {
        return UserResponse.from(repository.save(newUser(name, emailAddress, phone, channel)));
    }

    // ------------------- used by AuthService / DonationService -------------

    /**
     * Sign-up: same rules as createUser, plus the password hash and the role
     * profile the person picked. One save persists the User and (cascade) the profile.
     */
    User registerAccount(String name, String emailAddress, String phone,
                         NotificationChannel channel, String passwordHash, AccountRole role) {
        User user = newUser(name, emailAddress, phone, channel);
        user.setPasswordHash(passwordHash);
        switch (role != null ? role : AccountRole.DONOR) {
            case DONOR -> user.becomeDonor();
            case VOLUNTEER -> user.becomeVolunteer();
            case BENEFICIARY -> user.becomeBeneficiary();
        }
        return repository.save(user);
    }

    /** Login lookup by (normalized) email; the returned User has its strategy restored. */
    @Transactional(readOnly = true)
    Optional<User> findByEmail(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank()) {
            return Optional.empty();
        }
        return repository.findByEmail(normalizeEmail(emailAddress)).map(this::hydrate);
    }

    /** Managed, hydrated entity for other services (e.g. to attach as an Observer). */
    User loadUser(int id) {
        return find(id);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(int id) {
        return UserResponse.from(find(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(u -> UserResponse.from(hydrate(u)))
                .toList();
    }

    public void deleteUser(int id) {
        // Profiles are deleted with the user (composition: cascade ALL).
        repository.delete(find(id));
    }

    // ------------------------ STRATEGY: change channel ---------------------

    public UserResponse changeChannel(int id, NotificationChannel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel is required");
        }
        User user = find(id);
        user.setNotificationChannel(channel);
        user.setStrategy(strategyFor(channel)); // runtime swap
        return UserResponse.from(repository.save(user));
    }

    // ------------------------ OBSERVER entry point (demo) ------------------

    /** Delivers a message through the user's current strategy. */
    public void notifyUser(int id, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        find(id).notify(message);
    }

    // ------------------------ COMPOSITION: role profiles -------------------

    public UserResponse becomeVolunteer(int id) {
        User user = find(id);
        user.becomeVolunteer();
        return UserResponse.from(repository.save(user));
    }

    public UserResponse becomeDonor(int id) {
        User user = find(id);
        user.becomeDonor();
        return UserResponse.from(repository.save(user));
    }

    public UserResponse becomeBeneficiary(int id) {
        User user = find(id);
        user.becomeBeneficiary();
        return UserResponse.from(repository.save(user));
    }

    public UserResponse updateSkills(int id, Set<Skill> skills) {
        User user = find(id);
        if (user.getVolunteerProfile() == null) {
            throw new IllegalArgumentException("user is not a volunteer yet");
        }
        user.getVolunteerProfile().updateSkills(skills);
        return UserResponse.from(repository.save(user));
    }

    public UserResponse addNeed(int id, String need) {
        User user = find(id);
        if (user.getBeneficiaryProfile() == null) {
            throw new IllegalArgumentException("user is not a beneficiary yet");
        }
        user.getBeneficiaryProfile().addNeed(need);
        return UserResponse.from(repository.save(user));
    }

    // ------------------------------ helpers --------------------------------

    private User newUser(String name, String emailAddress, String phone, NotificationChannel channel) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (emailAddress == null || emailAddress.isBlank() || !emailAddress.contains("@")) {
            throw new IllegalArgumentException("a valid email is required");
        }
        String cleanEmail = normalizeEmail(emailAddress);
        if (repository.existsByEmail(cleanEmail)) {
            throw new DuplicateResourceException("email already registered: " + cleanEmail);
        }
        NotificationChannel chosen = (channel != null) ? channel : NotificationChannel.EMAIL;

        User user = new User(name.trim(), cleanEmail, phone, strategyFor(chosen));
        user.setNotificationChannel(chosen);
        return user;
    }

    private static String normalizeEmail(String emailAddress) {
        return emailAddress.trim().toLowerCase(Locale.ROOT);
    }

    private User find(int id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user " + id + " not found"));
        return hydrate(user);
    }

    /** Restores the live strategy object from the stored channel name. */
    private User hydrate(User user) {
        NotificationChannel channel = user.getNotificationChannel();
        user.setStrategy(strategyFor(channel != null ? channel : NotificationChannel.EMAIL));
        return user;
    }

    /** The single place that maps a channel name to its strategy object. */
    private NotificationStrategy strategyFor(NotificationChannel channel) {
        return switch (channel) {
            case EMAIL -> email;
            case SMS -> sms;
            case WHATSAPP -> whatsApp;
            case SOCIAL_MEDIA -> socialMedia;
        };
    }
}
