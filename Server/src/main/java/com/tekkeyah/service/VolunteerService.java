package com.tekkeyah.service;

import com.tekkeyah.exception.ResourceNotFoundException;
import com.tekkeyah.model.DTO.CreateVolunteerRequest;
import com.tekkeyah.model.DTO.UpdateVolunteerSkillsRequest;
import com.tekkeyah.model.DTO.UpdateVolunteerSkillsResponse;
import com.tekkeyah.model.DTO.VolunteerResponse;
import com.tekkeyah.model.DTO.VolunteerShiftResponse;
import com.tekkeyah.model.entity.Skill;
import com.tekkeyah.model.entity.Task;
import com.tekkeyah.model.entity.Volunteer;
import com.tekkeyah.repository.VolunteerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Volunteer use cases. Roles (Decorator chain) are built per shift from the task's required
 * skills, so there is no operation to assign a role directly.
 */
@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final Clock clock;

    public VolunteerService(VolunteerRepository volunteerRepository, Clock clock) {
        this.volunteerRepository = volunteerRepository;
        this.clock = clock;
    }

    @Transactional
    public VolunteerResponse create(CreateVolunteerRequest request) {
        if (request == null || isBlank(request.name()) || isBlank(request.email())) {
            throw new IllegalArgumentException("name and email are required");
        }
        Volunteer volunteer = new Volunteer();
        volunteer.setName(request.name().trim());
        volunteer.setEmail(request.email().trim());
        volunteer.setPhone(request.phone());
        volunteer.updateSkills(validSkills(request.skills()), LocalDateTime.now(clock));
        return VolunteerResponse.from(volunteerRepository.save(volunteer));
    }

    @Transactional(readOnly = true)
    public List<VolunteerResponse> findAll() {
        return volunteerRepository.findAll().stream()
                .map(VolunteerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VolunteerResponse findById(int id) {
        return VolunteerResponse.from(load(id));
    }

    /**
     * Replaces the volunteer's skills. The volunteer is removed from open tasks that need a skill
     * they no longer have, and their unfinished shifts on those tasks are cancelled.
     */
    @Transactional
    public UpdateVolunteerSkillsResponse updateSkills(int id, UpdateVolunteerSkillsRequest request) {
        Volunteer volunteer = load(id);
        List<Task> dropped = volunteer.updateSkills(
                validSkills(request == null ? null : request.skills()), LocalDateTime.now(clock));
        return new UpdateVolunteerSkillsResponse(VolunteerResponse.from(volunteer), dropped.size());
    }

    /** The volunteer's shifts, each with the role it grants and whether it is active now. */
    @Transactional(readOnly = true)
    public List<VolunteerShiftResponse> findShifts(int id) {
        LocalDateTime now = LocalDateTime.now(clock);
        return load(id).getShifts().stream()
                .map(s -> VolunteerShiftResponse.from(s, now))
                .toList();
    }

    @Transactional
    public void delete(int id) {
        volunteerRepository.delete(load(id));
    }

    private Volunteer load(int id) {
        return volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer", id));
    }

    private static Set<Skill> validSkills(Set<Skill> skills) {
        if (skills == null) {
            return Set.of();
        }
        if (skills.contains(null)) {
            throw new IllegalArgumentException("skills must not contain null");
        }
        return skills;
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
