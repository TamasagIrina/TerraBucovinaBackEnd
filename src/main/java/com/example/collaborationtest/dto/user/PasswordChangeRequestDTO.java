package com.example.collaborationtest.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload to request a password change. The new password is not applied
 * immediately — it is staged as {@code pendingPassword} until the user
 * confirms it via the link sent to their email.
 */
public record PasswordChangeRequestDTO(

        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String newPassword
) {
}
