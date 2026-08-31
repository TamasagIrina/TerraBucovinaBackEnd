package com.example.collaborationtest.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for a logged-in user editing their own profile (username, full name,
 * address). Email and password are deliberately excluded — email changes
 * aren't supported here, and password changes go through
 * {@link PasswordChangeRequestDTO} because they require email confirmation.
 */
public record UserSelfUpdateDTO(

        @NotBlank(message = "Username is required")
        @Size(max = 100, message = "Username must not exceed 100 characters")
        String username,

        @Size(max = 150, message = "Full name must not exceed 150 characters")
        String fullName,

        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address
) {
}
