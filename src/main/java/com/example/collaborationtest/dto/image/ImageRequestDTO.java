package com.example.collaborationtest.dto.image;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Metadata for an image upload. The binary is sent as a multipart file; this
 * record carries the descriptive fields. {@code imageUrl} is produced by the
 * server after the file is stored, so it is not part of the request.
 */
public record ImageRequestDTO(

        @NotNull(message = "Product id is required")
        Integer productId,

        @Size(max = 200, message = "Alt text must not exceed 200 characters")
        String altText,

        Integer sortOrder,

        Boolean isPrimary
) {
}
