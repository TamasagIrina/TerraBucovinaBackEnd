package com.example.collaborationtest.dto.order;

import java.math.BigDecimal;

/**
 * Read model for an {@code OrderProduct} line. The parent order is referenced by
 * {@code orderId} only (no back-reference), and the product is flattened to a
 * few useful fields so the line renders without a further lookup.
 */
public record OrderProductResponseDTO(
        int id,
        Integer orderId,
        Integer productId,
        String productName,
        BigDecimal productPrice,
        Integer quantity
) {
}
