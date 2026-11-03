package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.decorator.VolunteerRoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A volunteer. Skills are self-declared and decide which tasks they qualify for.
 * Roles (Decorator chain) are not held here: each {@link VolunteerShift} builds the role
 * for that task and time window, so permissions only apply while the volunteer is on shift.
 */
@Entity
@Table(name = "volunteers")
public class Volunteer extends User {

    @ElementCollection
    @CollectionTable(name = "volunteer_skills", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill", nullable = false, length = 32)
    private Set<Skill> skills = EnumSet.noneOf(Skill.class);

    @Column(name = "total_hours", nullable = false)
    private double totalHours;

    @ManyToMany
    @JoinTable(name = "volunteer_events",
            joinColumns = @JoinColumn(name = "volunteer_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> assignedEvents = new ArrayList<>();

    /** Tasks this volunteer works on; Task owns the relationship through its {@code assignees}. */
    @ManyToMany(mappedBy = "assignees")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VolunteerShift> shifts = new ArrayList<>();

    public Volunteer() {
        // required by JPA; starts with no skills
    }

    /**
     * Joins {@code task} for [start, end). The volunteer must have at least one required skill,
     * the task must be open, and the window must not overlap another of their shifts.
     */
    public VolunteerShift takeShift(Task task, LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(task, "task must not be null");
        if (task.isCompleted()) {
            throw new IllegalStateException("task is already completed");
        }
        if (!qualifiesFor(task)) {
            throw new IllegalStateException("volunteer has none of the skills this task requires");
        }
        VolunteerShift shift = new VolunteerShift(this, task, start, end);
        for (VolunteerShift existing : shifts) {
            if (existing.overlaps(start, end)) {
                throw new IllegalStateException("shift overlaps another shift from "
                        + existing.getStart() + " to " + existing.getEnd());
            }
        }
        shifts.add(shift);
        if (!tasks.contains(task)) {
            task.assign(this);
            tasks.add(task);
        }
        return shift;
    }

    /** True if a shift active at {@code at} grants {@code permission}. */
    public boolean hasPermissionAt(Permission permission, LocalDateTime at) {
        return shifts.stream()
                .filter(s -> s.isActive(at))
                .anyMatch(s -> s.getRole().getPermissions().contains(permission));
    }

    /** Roles this volunteer's skills can unlock on a shift (for display, e.g. "Can be: Driver, Cook"). */
    public List<VolunteerRoleType> getPossibleRoles() {
        return VolunteerRoleType.decompose(VolunteerRoleType.roleFor(skills));
    }

    /**
     * Replaces the volunteer's skills and unassigns them from every open (not completed) task they
     * no longer qualify for, cancelling their shifts on that task that have not ended by {@code now}.
     *
     * @return the tasks the volunteer was removed from
     */
    public List<Task> updateSkills(Set<Skill> newSkills, LocalDateTime now) {
        skills.clear();
        if (newSkills != null) {
            skills.addAll(newSkills);
        }
        List<Task> dropped = new ArrayList<>();
        for (Task task : List.copyOf(tasks)) {
            if (!task.isCompleted() && !qualifiesFor(task)) {
                task.unassign(this);
                tasks.remove(task);
                shifts.removeIf(s -> s.getTask() == task && s.getEnd().isAfter(now));
                dropped.add(task);
            }
        }
        return dropped;
    }

    /** A task with no required skills is open to everyone; otherwise one matching skill is enough. */
    public boolean qualifiesFor(Task task) {
        Set<Skill> required = task.getRequiredSkills();
        return required.isEmpty() || required.stream().anyMatch(skills::contains);
    }

    /** Volunteer chooses an event to attend. Choosing the same event twice is a no-op. */
    public void assignTo(Event e) {
        Objects.requireNonNull(e, "event must not be null");
        if (!assignedEvents.contains(e)) {
            assignedEvents.add(e);
        }
    }

    /** Adds the hours of a completed shift to this volunteer's total. */
    public void logHours(VolunteerShift s) {
        Objects.requireNonNull(s, "shift must not be null");
        if (s.getVolunteer() != this) {
            throw new IllegalArgumentException("shift belongs to another volunteer");
        }
        totalHours += s.getHours();
    }

    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public double getTotalHours() {
        return totalHours;
    }

    public List<Event> getAssignedEvents() {
        return Collections.unmodifiableList(assignedEvents);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public List<VolunteerShift> getShifts() {
        return Collections.unmodifiableList(shifts);
    }
}
