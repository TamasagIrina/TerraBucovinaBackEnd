package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.order.OrderProductResponseDTO;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts {@link OrderProduct} line items to their read model. The parent
 * order is exposed as an id (no back-reference) and the product is flattened.
 */
@Component
public class OrderProductMapper {

    public OrderProductResponseDTO toResponse(OrderProduct op) {
        if (op == null) {
            return null;
        }
        Product product = op.getProduct();
        return new OrderProductResponseDTO(
                op.getId(),
                op.getOrder() != null ? op.getOrder().getId() : null,
                product != null ? product.getId() : null,
                product != null ? product.getName() : null,
                product != null ? product.getPrice() : null,
                op.getQuantity()
        );
    }

    public List<OrderProductResponseDTO> toResponseList(List<OrderProduct> items) {
        return Optional.ofNullable(items).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
