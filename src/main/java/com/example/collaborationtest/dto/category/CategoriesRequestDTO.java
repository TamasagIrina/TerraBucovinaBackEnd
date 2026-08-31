package com.example.collaborationtest.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating / updating a {@code Categories} row.
 */
public record CategoriesRequestDTO(

        @NotBlank(message = "Category name is required")
        @Size(max = 200, message = "Category name must not exceed 200 characters")
        String name,

        @NotBlank(message = "Category description is required")
        @Size(max = 500, message = "Category description must not exceed 500 characters")
        String description
) {
}
