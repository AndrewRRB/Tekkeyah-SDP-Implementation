package com.tekkeyah.controller;

import com.tekkeyah.model.DTO.AuthResponse;
import com.tekkeyah.model.DTO.LoginRequest;
import com.tekkeyah.model.DTO.SignUpRequest;
import com.tekkeyah.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ENDPOINTS
 *   POST /api/auth/signup   create an account (+ role profile)   -> 201
 *   POST /api/auth/login    check email + password              -> 200 / 401
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(req));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
