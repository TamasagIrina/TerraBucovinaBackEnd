package com.example.collaborationtest.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for submitting a contact-us message. Status is assigned by the server.
 */
public record ContactUsMessagesRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(max = 200, message = "Name must not exceed 200 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 200, message = "Email must not exceed 200 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Size(max = 50, message = "Phone number must not exceed 50 characters")
        String phone_number,

        @Size(max = 500, message = "Subject must not exceed 500 characters")
        String subject,

        @Size(max = 1000, message = "Message must not exceed 1000 characters")
        String message
) {
}
