package com.tekkeyah.controller;

import com.tekkeyah.model.DTO.CreateVolunteerRequest;
import com.tekkeyah.model.DTO.UpdateVolunteerSkillsRequest;
import com.tekkeyah.model.DTO.UpdateVolunteerSkillsResponse;
import com.tekkeyah.model.DTO.VolunteerResponse;
import com.tekkeyah.model.DTO.VolunteerShiftResponse;
import com.tekkeyah.service.VolunteerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    public ResponseEntity<VolunteerResponse> create(@RequestBody CreateVolunteerRequest request) {
        VolunteerResponse created = volunteerService.create(request);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}").buildAndExpand(created.id()).toUri())
                .body(created);
    }

    @GetMapping
    public List<VolunteerResponse> findAll() {
        return volunteerService.findAll();
    }

    @GetMapping("/{id}")
    public VolunteerResponse findById(@PathVariable int id) {
        return volunteerService.findById(id);
    }

    /** Replace skills, e.g. {"skills":["COOKING","DRIVING"]}; unmatched open tasks are dropped. */
    @PutMapping("/{id}/skills")
    public UpdateVolunteerSkillsResponse updateSkills(@PathVariable int id,
                                                      @RequestBody UpdateVolunteerSkillsRequest request) {
        return volunteerService.updateSkills(id, request);
    }

    /** Shifts with the role each one grants, e.g. 18:00 Driver, 20:00 Cook. */
    @GetMapping("/{id}/shifts")
    public List<VolunteerShiftResponse> findShifts(@PathVariable int id) {
        return volunteerService.findShifts(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        volunteerService.delete(id);
    }
}
