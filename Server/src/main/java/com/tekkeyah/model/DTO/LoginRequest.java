package com.tekkeyah.model.DTO;

/** POST /api/auth/login body. "email" is the account's email address. */
public record LoginRequest(String email, String password) {
}
