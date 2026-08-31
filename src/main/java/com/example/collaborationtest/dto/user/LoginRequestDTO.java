package com.example.collaborationtest.dto.user;

import jakarta.validation.constraints.NotBlank;

/**
 * Credentials payload for the login endpoint. Kept separate from
 * {@code UserRequestDTO} because login neither needs a username nor the terms flag.
 */
public record LoginRequestDTO(

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {
}
