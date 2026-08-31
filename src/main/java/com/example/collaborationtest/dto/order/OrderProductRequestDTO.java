package com.example.collaborationtest.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * A single line inside an {@code OrderRequestDTO}: which product and how many.
 */
public record OrderProductRequestDTO(

        @NotNull(message = "Product id is required")
        Integer productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
