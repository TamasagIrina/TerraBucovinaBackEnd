package com.example.collaborationtest.dto.order;

import com.example.collaborationtest.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Read model for an {@code Order}. The placing user is exposed as {@code userId}
 * only (never the nested {@code User}, which would leak credentials), and line
 * items are slim {@link OrderProductResponseDTO}s that do not point back to the order.
 */
public record OrderResponseDTO(
        int id,
        String fullName,
        String email,
        String phone,
        Boolean isCompanyInvoice,
        String cui,
        String country,
        String county,
        String city,
        String postalCode,
        String paymentMethod,
        String deliveryMethod,
        Boolean termsAccepted,
        LocalDateTime termsAcceptedAt,
        String termsVersion,
        String address,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        OrderStatus status,
        Integer userId,
        List<OrderProductResponseDTO> products
) {
}
