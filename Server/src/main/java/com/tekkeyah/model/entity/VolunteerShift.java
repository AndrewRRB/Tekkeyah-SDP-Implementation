package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.decorator.VolunteerRole;
import com.tekkeyah.pattern.decorator.VolunteerRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * One volunteer working one task in a time window. The volunteer's role (Decorator chain)
 * belongs to the shift, not to the volunteer: it is built from the skills this task needs
 * that the volunteer has, and its permissions only apply while the shift is active.
 * <p>
 * Example: a task needing DRIVING gives Driver(Basic) for that shift; a task needing
 * DRIVING and COOKING gives Cook(Driver(Basic)).
 */
@Entity
@Table(name = "volunteer_shifts")
public class VolunteerShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shiftID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    protected VolunteerShift() {
        // required by JPA
    }

    /** Created through {@link Volunteer#takeShift}, which checks skills and overlaps. */
    VolunteerShift(Volunteer volunteer, Task task, LocalDateTime start, LocalDateTime end) {
        this.volunteer = Objects.requireNonNull(volunteer, "volunteer must not be null");
        this.task = Objects.requireNonNull(task, "task must not be null");
        this.start = Objects.requireNonNull(start, "start must not be null");
        this.end = Objects.requireNonNull(end, "end must not be null");
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("shift end must be after start");
        }
    }

    /** Role for this shift: the task's required skills that the volunteer has, as decorators on Basic. */
    public VolunteerRole getRole() {
        Set<Skill> used = EnumSet.noneOf(Skill.class);
        used.addAll(task.getRequiredSkills());
        used.retainAll(volunteer.getSkills());
        return VolunteerRoleType.roleFor(used);
    }

    /** True while {@code at} is inside [start, end). */
    public boolean isActive(LocalDateTime at) {
        return !at.isBefore(start) && at.isBefore(end);
    }

    /** True if the two time windows intersect. */
    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return start.isBefore(otherEnd) && otherStart.isBefore(end);
    }

    public double getHours() {
        return Duration.between(start, end).toMinutes() / 60.0;
    }

    public Integer getShiftID() {
        return shiftID;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public Task getTask() {
        return task;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
