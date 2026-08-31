package com.example.collaborationtest.dto.product;

import com.example.collaborationtest.dto.image.ImageResponseDTO;
import com.example.collaborationtest.dto.plant.PlantResponseDTO;
import com.example.collaborationtest.dto.review.ReviewResponseDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Read model returned to clients for a {@code Product} (GET / POST / PUT responses).
 * <p>
 * Relationships are shaped to break the entity's bidirectional links and prevent
 * infinite recursion during serialization:
 * <ul>
 *   <li>the parent {@code Categories} is flattened to {@code categoryId} + {@code categoryName};</li>
 *   <li>child collections are exposed as slim nested DTOs
 *       ({@link PlantResponseDTO}, {@link ImageResponseDTO}, {@link ReviewResponseDTO}),
 *       none of which carry a back-reference to the product — so the graph is acyclic.</li>
 * </ul>
 */
public record ProductResponseDTO(
        int id,
        String name,
        BigDecimal price,
        String shortDesc,
        String longDesc,
        String notification,
        String ingredients,
        String scientificStudies,
        Integer stockQty,
        boolean active,
        String mainImageUrl,
        String createdAt,
        String updatedAt,
        Integer categoryId,
        String categoryName,
        List<PlantResponseDTO> plants,
        List<ImageResponseDTO> images,
        List<ReviewResponseDTO> reviews
) {
}
