package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.order.OrderRequestDTO;
import com.example.collaborationtest.dto.order.OrderResponseDTO;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.model.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts between {@link Order} and its DTOs.
 * <p>
 * {@link #toEntity} produces a detached entity graph with <em>stub</em> products
 * (id only) and line quantities; the service is responsible for resolving those
 * products/the user against the database, wiring the {@code order} back-reference,
 * and setting server-managed fields (status, timestamps, terms).
 */
@Component
public class OrderMapper {

    private final OrderProductMapper orderProductMapper;

    public OrderMapper(OrderProductMapper orderProductMapper) {
        this.orderProductMapper = orderProductMapper;
    }

    public Order toEntity(OrderRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Order order = Order.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .phone(dto.phone())
                .isCompanyInvoice(dto.isCompanyInvoice())
                .cui(dto.cui())
                .country(dto.country())
                .county(dto.county())
                .city(dto.city())
                .postalCode(dto.postalCode())
                .paymentMethod(dto.paymentMethod())
                .deliveryMethod(dto.deliveryMethod())
                .termsAccepted(dto.termsAccepted())
                .address(dto.address())
                .totalPrice(dto.totalPrice())
                .build();

        var lines = Optional.ofNullable(dto.products()).orElseGet(java.util.List::of)
                .stream()
                .map(line -> OrderProduct.builder()
                        .product(Product.builder().id(line.productId()).build())
                        .quantity(line.quantity())
                        .build())
                .collect(Collectors.toList());
        order.setProducts(lines);

        return order;
    }

    public OrderResponseDTO toResponse(Order order) {
        if (order == null) {
            return null;
        }
        Integer userId = order.getUser() != null ? order.getUser().getId() : null;
        return new OrderResponseDTO(
                order.getId(),
                order.getFullName(),
                order.getEmail(),
                order.getPhone(),
                order.getIsCompanyInvoice(),
                order.getCui(),
                order.getCountry(),
                order.getCounty(),
                order.getCity(),
                order.getPostalCode(),
                order.getPaymentMethod(),
                order.getDeliveryMethod(),
                order.getTermsAccepted(),
                order.getTermsAcceptedAt(),
                order.getTermsVersion(),
                order.getAddress(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus(),
                userId,
                orderProductMapper.toResponseList(order.getProducts())
        );
    }

    public java.util.List<OrderResponseDTO> toResponseList(java.util.List<Order> orders) {
        return Optional.ofNullable(orders).orElseGet(java.util.List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
