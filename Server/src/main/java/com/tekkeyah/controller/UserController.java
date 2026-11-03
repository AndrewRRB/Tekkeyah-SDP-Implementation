package com.tekkeyah.controller;

import com.tekkeyah.model.DTO.ChannelRequest;
import com.tekkeyah.model.DTO.CreateUserRequest;
import com.tekkeyah.model.DTO.MessageRequest;
import com.tekkeyah.model.DTO.NeedRequest;
import com.tekkeyah.model.DTO.SkillsRequest;
import com.tekkeyah.model.DTO.UserResponse;
import com.tekkeyah.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ============================================================================
 * ENDPOINTS AND THE PATTERN EACH ONE SHOWS
 *   POST   /api/users                      create user         (Strategy chosen)
 *   GET    /api/users                      list users
 *   GET    /api/users/{id}                 one user
 *   DELETE /api/users/{id}                 delete user + profiles (Composition)
 *   PUT    /api/users/{id}/channel         swap channel        (STRATEGY)
 *   POST   /api/users/{id}/notify          send a message      (OBSERVER -> Strategy)
 *   POST   /api/users/{id}/volunteer       add volunteer role  (COMPOSITION)
 *   POST   /api/users/{id}/donor           add donor role      (COMPOSITION)
 *   POST   /api/users/{id}/beneficiary     add beneficiary role(COMPOSITION)
 *   PUT    /api/users/{id}/volunteer/skills   set skills       (-> DECORATOR later)
 *   POST   /api/users/{id}/beneficiary/needs  add a need
 *
 * HOW IT CONNECTS
 *   Request DTOs in -> UserService -> UserResponse DTO out. Errors are turned
 *   into JSON by GlobalExceptionHandler. CORS for the React client is already
 *   configured in config/CorsConfig.
 * ============================================================================
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest req) {
        UserResponse created = service.createUser(req.name(), req.email(), req.phone(), req.channel());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<UserResponse> all() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse one(@PathVariable int id) {
        return service.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/channel")
    public UserResponse changeChannel(@PathVariable int id, @RequestBody ChannelRequest req) {
        return service.changeChannel(id, req.channel());
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<Void> notifyUser(@PathVariable int id, @RequestBody MessageRequest req) {
        service.notifyUser(id, req.message());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/volunteer")
    public UserResponse becomeVolunteer(@PathVariable int id) {
        return service.becomeVolunteer(id);
    }

    @PostMapping("/{id}/donor")
    public UserResponse becomeDonor(@PathVariable int id) {
        return service.becomeDonor(id);
    }

    @PostMapping("/{id}/beneficiary")
    public UserResponse becomeBeneficiary(@PathVariable int id) {
        return service.becomeBeneficiary(id);
    }

    @PutMapping("/{id}/volunteer/skills")
    public UserResponse updateSkills(@PathVariable int id, @RequestBody SkillsRequest req) {
        return service.updateSkills(id, req.skills());
    }

    @PostMapping("/{id}/beneficiary/needs")
    public UserResponse addNeed(@PathVariable int id, @RequestBody NeedRequest req) {
        return service.addNeed(id, req.need());
    }
}
