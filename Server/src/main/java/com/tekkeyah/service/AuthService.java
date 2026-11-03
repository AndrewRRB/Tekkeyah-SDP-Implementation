package com.tekkeyah.service;

import com.tekkeyah.exception.InvalidCredentialsException;
import com.tekkeyah.model.DTO.AuthResponse;
import com.tekkeyah.model.DTO.LoginRequest;
import com.tekkeyah.model.DTO.SignUpRequest;
import com.tekkeyah.model.DTO.UserResponse;
import com.tekkeyah.model.entity.User;
import com.tekkeyah.util.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================================
 * PART : Sign up and log in (UML: User.login(username, password), passwordHash)
 * ----------------------------------------------------------------------------
 * Sign up = UserService's normal user rules + a salted password hash + the
 * role profile the person picked (composition). Login = find by email and
 * compare hashes. Phase 1 is stateless: the client keeps the returned user id.
 * Spring Security / tokens can wrap this later without changing callers.
 * ============================================================================
 */
@Service
@Transactional
public class AuthService {

    static final int MIN_PASSWORD_LENGTH = 8;
    private static final String BAD_CREDENTIALS = "invalid email or password";

    private final UserService userService;
    private final PasswordHasher passwordHasher;

    public AuthService(UserService userService, PasswordHasher passwordHasher) {
        this.userService = userService;
        this.passwordHasher = passwordHasher;
    }

    public AuthResponse signUp(SignUpRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("request body is required");
        }
        String password = req.password();
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        User saved = userService.registerAccount(req.fullName(), req.email(), req.phone(),
                req.channel(), passwordHasher.hash(password), req.role());
        saved.notify("Welcome to Tekkeyah, " + saved.getName() + "!");
        return AuthResponse.from(UserResponse.from(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        if (req == null || req.password() == null) {
            throw new InvalidCredentialsException(BAD_CREDENTIALS);
        }
        User user = userService.findByEmail(req.email())
                .filter(u -> passwordHasher.matches(req.password(), u.getPasswordHash()))
                .orElseThrow(() -> new InvalidCredentialsException(BAD_CREDENTIALS));
        return AuthResponse.from(UserResponse.from(user));
    }
}
