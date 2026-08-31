package com.example.collaborationtest.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payload for placing an {@code Order}. Server-managed fields (id, status,
 * createdAt, termsAcceptedAt, termsVersion) are set by the service, not the client.
 * {@code userId} is optional to allow guest checkout. Nested lines are validated
 * via {@link Valid}.
 */
public record OrderRequestDTO(

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Phone is required")
        String phone,

        Boolean isCompanyInvoice,

        String cui,

        String country,

        String county,

        String city,

        String postalCode,

        String paymentMethod,

        String deliveryMethod,

        @NotNull(message = "Terms acceptance flag is required")
        Boolean termsAccepted,

        String address,

        @NotNull(message = "Total price is required")
        BigDecimal totalPrice,

        Integer userId,

        @NotEmpty(message = "An order must contain at least one product")
        @Valid
        List<OrderProductRequestDTO> products
) {
}
