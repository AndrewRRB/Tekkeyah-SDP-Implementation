package com.tekkeyah.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.tekkeyah.model.enums.Skill;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * PATTERN : COMPOSITION ("role profile") - the User-side half of the design
 *           (this class is also the entry point into the DECORATOR pattern)
 * PART    : VolunteerProfile, in the USERS box of the diagram
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   The "volunteer" side of a person. It is NOT a subclass of User. A User
 *   owns at most one of these (0..1). That is why one person can be a
 *   volunteer AND a donor at the same time: the roles are optional parts of
 *   the account, not different kinds of account.
 *
 * HOW IT CONNECTS
 *   - User.becomeVolunteer() creates it and keeps it (filled diamond = the
 *     profile cannot exist without its User; delete the User, it goes too).
 *   - skills (Skill enum) will decide the volunteer's role through the
 *     DECORATOR pattern: role = VolunteerRoleType.roleFor(skills) builds
 *     BasicVolunteerRole wrapped by DriverRole / CookRole layers.
 *   - Notifications are NOT duplicated here: the User is the Observer and owns
 *     the NotificationStrategy, so this profile just uses user.notify(...).
 *
 * WIRED LATER (types live on other branches, so they are left out for now so
 * this branch compiles on its own). Uncomment when merging "Volunteer":
 *   private VolunteerRole role;                    // derived from skills
 *   private List<Event> assignedEvents;            // @ManyToMany, 0..*
 *   public VolunteerRole getRole()
 *   public List<Task> updateSkills(Set<Skill> s)   // returns tasks the
 *                                                   // volunteer was removed from
 *   public boolean qualifiesFor(Task t)            // any ONE matching skill
 *   public void assignTo(Event e)
 *   public void logHours(VolunteerShift s)         // calls addHours(s.getHours())
 * ============================================================================
 */
@Entity
@Table(name = "volunteer_profiles")
public class VolunteerProfile {

    // Surrogate key (JPA needs an id; the diagram does not show one).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_id")
    private int volunteerID;

    // The owning side of the User <-> Profile link (stored as a foreign key).
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "volunteer_skills", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    private Set<Skill> skills = new HashSet<>();

    @Column(name = "total_hours")
    private double totalHours;

    public VolunteerProfile() {}

    public VolunteerProfile(User user) {
        this.user = user;
    }

    /**
     * Replaces the declared skills. (Later: also rebuilds the role chain and
     * returns the tasks the volunteer no longer qualifies for.)
     */
    public void updateSkills(Set<Skill> newSkills) {
        this.skills.clear();
        if (newSkills != null) {
            this.skills.addAll(newSkills);
        }
    }

    /** Helper that logHours(VolunteerShift) will call once VolunteerShift exists. */
    public void addHours(double hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("hours must be >= 0");
        }
        this.totalHours += hours;
    }

    public int getVolunteerID() { return volunteerID; }

    public User getUser() { return user; }

    public Set<Skill> getSkills() { return skills; }

    public double getTotalHours() { return totalHours; }
}
