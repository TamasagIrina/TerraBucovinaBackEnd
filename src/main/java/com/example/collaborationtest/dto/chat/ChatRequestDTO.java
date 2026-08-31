package com.example.collaborationtest.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Incoming chatbot message: the problem the customer describes.
 */
public record ChatRequestDTO(
        @NotBlank(message = "Mesajul nu poate fi gol")
        @Size(max = 2000, message = "Mesajul este prea lung")
        String message
) {
}
