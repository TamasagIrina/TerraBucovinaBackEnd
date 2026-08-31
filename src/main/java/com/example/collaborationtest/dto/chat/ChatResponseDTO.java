package com.example.collaborationtest.dto.chat;

import java.math.BigDecimal;
import java.util.List;

/**
 * Chatbot answer: a natural-language reply plus the real products recommended
 * (looked up from the DB, so the client always gets valid catalog items).
 */
public record ChatResponseDTO(
        String reply,
        List<RecommendedProduct> products
) {
    public record RecommendedProduct(
            int id,
            String name,
            BigDecimal price,
            String mainImageUrl
    ) {
    }
}
