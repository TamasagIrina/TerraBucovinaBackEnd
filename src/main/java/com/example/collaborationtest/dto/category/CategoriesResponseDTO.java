package com.example.collaborationtest.dto.category;

import java.util.List;

/**
 * Read model for a {@code Categories} row. Products are exposed as an id list
 * rather than nested product DTOs: a product is an aggregate root in its own
 * right, and inlining full products here would produce heavy, redundant payloads.
 */
public record CategoriesResponseDTO(
        int id,
        String name,
        String description,
        List<Integer> productIds
) {
}
