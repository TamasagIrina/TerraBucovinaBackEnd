package com.example.collaborationtest.dto.plant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating a {@code Plant}. The image itself is uploaded as a
 * multipart file alongside this metadata, so {@code imageUrl} is not part of
 * the request.
 */
public record PlantRequestDTO(

        @NotBlank(message = "Plant name is required")
        @Size(max = 200, message = "Plant name must not exceed 200 characters")
        String name,

        @Size(max = 65535, message = "Short description is too long")
        String shortDescription,

        @Size(max = 1000, message = "Long description must not exceed 1000 characters")
        String longDescription,

        @Size(max = 300, message = "Plant message must not exceed 300 characters")
        String plantMessage,

        @NotNull(message = "Product id is required")
        Integer productId
) {
}
