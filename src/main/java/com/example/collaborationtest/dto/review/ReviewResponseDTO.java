package com.example.collaborationtest.dto.review;

import java.time.Instant;

/**
 * Read model for a {@code Review}. Related product and user are exposed as ids
 * only — never as nested entities — so the user's credentials are never leaked
 * and the graph stays acyclic when nested inside {@code ProductResponseDTO}.
 */
public record ReviewResponseDTO(
        int id,
        Integer productId,
        Integer userId,
        String body,
        Integer stars,
        Instant createdAt
) {
}
