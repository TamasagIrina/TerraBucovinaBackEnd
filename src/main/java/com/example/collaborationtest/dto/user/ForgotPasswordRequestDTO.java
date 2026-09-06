package com.example.collaborationtest.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for the "forgot password" flow (user isn't logged in and doesn't
 * know their current password). The new password is staged and only applied
 * once the user confirms it via the link sent to their email — see
 * {@code UserService#requestForgotPassword} and the existing, reused
 * {@code AuthController#confirmPasswordChange}.
 */
public record ForgotPasswordRequestDTO(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String newPassword
) {
}
