package com.tekkeyah.model.entity;

import java.util.Observable;
import java.util.Observer;

import com.tekkeyah.model.enums.NotificationChannel;
import com.tekkeyah.pattern.strategy.NotificationStrategy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * ============================================================================
 * PATTERNS : OBSERVER (this class is the OBSERVER)
 *            STRATEGY (this class is the CONTEXT that holds a strategy)
 *            COMPOSITION (owns optional role profiles)
 * PART     : User, the centre of the USERS box of the diagram
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   One account per person. The role (volunteer / donor / beneficiary) is NOT a
 *   subclass: each is an optional profile (0..1) that the User owns. Admin is
 *   the only subclass.
 *
 * OBSERVER  (uses Java's built-in java.util.Observer, not our own interface)
 *   Subjects (Event, Donation, Payment - other branches) extend
 *   java.util.Observable. When their state changes they call
 *   setChanged(); notifyObservers(msg). Java then calls THIS class's
 *   update(source, msg). The diagram's attach/detach/notifyObservers map to
 *   Observable.addObserver / deleteObserver / notifyObservers.
 *
 * STRATEGY
 *   update() does not decide HOW to deliver the message; it hands it to the
 *   current NotificationStrategy (Email / SMS / WhatsApp / SocialMedia).
 *   setStrategy() swaps the channel at runtime without changing this class.
 *   The chosen channel is saved as notificationChannel (an enum name) because
 *   the strategy object itself cannot be stored; UserService rebuilds it.
 *   Observer decides WHEN to notify, Strategy decides HOW - that is why
 *   notifications are never duplicated in the role profiles.
 *
 * COMPOSITION
 *   The three profiles use a filled diamond: a profile cannot exist without
 *   its User (cascade ALL + orphanRemoval). becomeVolunteer() / becomeDonor() /
 *   becomeBeneficiary() create the profile on demand and are safe to call
 *   twice (they return the existing one).
 *
 * HOW IT CONNECTS
 *   - pattern/strategy/*          : the strategy classes call back user.getEmail()
 *                                   / getName() / getPhone() to deliver.
 *   - VolunteerProfile, DonorProfile, BeneficiaryProfile : owned by this class.
 *   - Admin                       : the only subclass (JOINED inheritance).
 *   - Event / Donation / Payment  : the Subjects that notify this class.
 *
 * NOTE: java.util.Observer is deprecated since Java 9 (still fully supported).
 * The suppression below only hides that warning.
 * ============================================================================
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("deprecation")
public class User implements Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    // UML: - passwordHash: String. Salted hash only, never the raw password
    // (see util/PasswordHasher). Nullable so users inserted without one still load.
    @Column(name = "password_hash")
    private String passwordHash;

    // STRATEGY: the strategy OBJECT is not stored (it is a live object)...
    @Transient
    protected NotificationStrategy strategy;

    // ...so its NAME is stored instead. UserService turns this name back into
    // the real strategy object whenever a User is loaded or the channel changes.
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_channel") // nullable on purpose: old rows read as EMAIL
    private NotificationChannel notificationChannel = NotificationChannel.EMAIL;

    // COMPOSITION: each profile is optional (0..1) and owned by this User.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VolunteerProfile volunteerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private DonorProfile donorProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private BeneficiaryProfile beneficiaryProfile;

    public User() {}

    public User(String name, String email, String phone, NotificationStrategy strategy) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.strategy = strategy;
    }

    // ----------------------------- STRATEGY --------------------------------

    /** Swap the delivery channel at runtime (Strategy pattern). */
    public void setStrategy(NotificationStrategy s) {
        this.strategy = s;
    }

    public NotificationStrategy getStrategy() {
        return strategy;
    }

    public NotificationChannel getNotificationChannel() { return notificationChannel; }

    public void setNotificationChannel(NotificationChannel channel) { this.notificationChannel = channel; }

    /** Sends msg through whichever strategy is currently set. */
    public void notify(String msg) {
        if (strategy != null) {
            strategy.send(this, msg);
        } else {
            System.out.println("[WARNING] No notification strategy set for user: " + name);
        }
    }

    // ----------------------------- OBSERVER --------------------------------

    /**
     * Called by the built-in Observable when a Subject this user is attached to
     * changes. Delegates delivery to the Strategy.
     */
    @Override
    public void update(Observable source, Object msg) {
        notify(String.valueOf(msg));
    }

    // --------------------------- COMPOSITION -------------------------------

    public VolunteerProfile becomeVolunteer() {
        if (volunteerProfile == null) {
            volunteerProfile = new VolunteerProfile(this);
        }
        return volunteerProfile;
    }

    public DonorProfile becomeDonor() {
        if (donorProfile == null) {
            donorProfile = new DonorProfile(this);
        }
        return donorProfile;
    }

    public BeneficiaryProfile becomeBeneficiary() {
        if (beneficiaryProfile == null) {
            beneficiaryProfile = new BeneficiaryProfile(this);
        }
        return beneficiaryProfile;
    }

    public VolunteerProfile getVolunteerProfile() { return volunteerProfile; }

    public DonorProfile getDonorProfile() { return donorProfile; }

    public BeneficiaryProfile getBeneficiaryProfile() { return beneficiaryProfile; }

    // --------------------------- GETTERS / SETTERS -------------------------

    public int getUserID() { return userID; }

    public void setUserID(int userID) { this.userID = userID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
