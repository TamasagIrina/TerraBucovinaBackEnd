package com.example.collaborationtest.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Payload for creating / updating a {@code Product} (POST / PUT).
 * <p>
 * Only client-settable fields are exposed. Server-managed values
 * ({@code id}, {@code createdAt}, {@code updatedAt}) and child collections
 * (plants, images, reviews — managed through their own endpoints) are omitted.
 * The parent category is referenced by id only to avoid pulling the whole
 * entity graph across the wire.
 */
public record ProductRequestDTO(

        @NotBlank(message = "Product name is required")
        @Size(max = 200, message = "Product name must not exceed 200 characters")
        String name,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must not be negative")
        @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer and 2 fractional digits")
        BigDecimal price,

        @Size(max = 500, message = "Short description must not exceed 500 characters")
        String shortDesc,

        @Size(max = 1000, message = "Long description must not exceed 1000 characters")
        String longDesc,

        @Size(max = 200, message = "Notification must not exceed 200 characters")
        String notification,

        @Size(max = 200, message = "Ingredients must not exceed 200 characters")
        String ingredients,

        @Size(max = 2000, message = "Scientific studies must not exceed 2000 characters")
        String scientificStudies,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must not be negative")
        Integer stockQty,

        @Size(max = 500, message = "Main image URL must not exceed 500 characters")
        String mainImageUrl,

        @NotNull(message = "Category id is required")
        Integer categoryId
) {
}
