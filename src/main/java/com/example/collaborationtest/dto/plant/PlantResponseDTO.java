package com.example.collaborationtest.dto.plant;

/**
 * Read model for a {@code Plant}. The owning product is referenced by
 * {@code productId} only — no back-reference to the product object — so the
 * DTO can be safely nested inside {@code ProductResponseDTO} without recursion.
 */
public record PlantResponseDTO(
        int id,
        Integer productId,
        String name,
        String imageUrl,
        String shortDescription,
        String longDescription,
        String plantMessage
) {
}
