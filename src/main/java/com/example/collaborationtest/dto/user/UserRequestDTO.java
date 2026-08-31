package com.example.collaborationtest.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for registering / creating a {@code User}. The raw password is
 * accepted here (it is hashed by the service before persistence) but is never
 * echoed back — see {@code UserResponseDTO}. Roles and the enabled flag are set
 * server-side, not by the client.
 */
public record UserRequestDTO(

        @NotBlank(message = "Username is required")
        @Size(max = 100, message = "Username must not exceed 100 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        boolean termsAccepted
) {
}
