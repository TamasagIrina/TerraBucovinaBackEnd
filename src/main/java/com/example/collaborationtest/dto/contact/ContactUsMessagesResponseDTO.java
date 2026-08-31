package com.example.collaborationtest.dto.contact;

import com.example.collaborationtest.enums.MessageStatus;

/**
 * Read model for a {@code ContactUsMessages} row.
 */
public record ContactUsMessagesResponseDTO(
        int id,
        String name,
        String email,
        String phone_number,
        String subject,
        String message,
        MessageStatus status
) {
}
