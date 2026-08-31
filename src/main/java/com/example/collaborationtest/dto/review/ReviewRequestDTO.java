package com.example.collaborationtest.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for submitting a {@code Review}. Replaces the former ad-hoc
 * {@code ReviewRequest} class and adds Bean Validation.
 */
public record ReviewRequestDTO(

        @NotNull(message = "Product id is required")
        Integer productId,

        @NotNull(message = "User id is required")
        Integer userId,

        @Size(max = 65535, message = "Review body is too long")
        String body,

        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        Integer stars
) {
}
