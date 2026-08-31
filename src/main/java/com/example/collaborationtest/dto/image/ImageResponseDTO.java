package com.example.collaborationtest.dto.image;

/**
 * Read model for an {@code Image}. Owning product is exposed as {@code productId}
 * only, keeping the DTO safe to nest inside {@code ProductResponseDTO}.
 */
public record ImageResponseDTO(
        int id,
        Integer productId,
        String imageUrl,
        String altText,
        Integer sortOrder,
        Boolean isPrimary
) {
}
