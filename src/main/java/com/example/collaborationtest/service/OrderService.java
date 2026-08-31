package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.order.OrderRequestDTO;
import com.example.collaborationtest.dto.order.OrderResponseDTO;
import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.mapper.OrderMapper;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.dto.common.PageResponse;
import com.example.collaborationtest.repository.OrderRepo;
import com.example.collaborationtest.repository.ProductRepo;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private OrderRepo orderRepo;
    private OrderProductService orderProductService;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepo orderRepo, OrderProductService orderProductService, ProductRepo productRepo,
                        UserRepo userRepo, EmailService emailService, OrderMapper orderMapper) {
        this.orderRepo = orderRepo;
        this.orderProductService = orderProductService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
    }

    public List<OrderResponseDTO> getAllOrder() {
        return orderMapper.toResponseList(orderRepo.findAll());
    }

    public PageResponse<OrderResponseDTO> getAllOrderPaged(OrderStatus status, Pageable pageable) {
        var page = (status == null)
                ? orderRepo.findAll(pageable)
                : orderRepo.findAllByStatus(status, pageable);
        return PageResponse.from(page.map(orderMapper::toResponse));
    }

    public PageResponse<OrderResponseDTO> getOrdersByCustomerPaged(int id, Pageable pageable) {
        return PageResponse.from(orderRepo.findByUserId(id, pageable).map(orderMapper::toResponse));
    }

    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderMapper.toResponseList(orderRepo.findAllByStatus(status));
    }

    public List<OrderResponseDTO> getOrdersByCustomer(int id) {
        return orderMapper.toResponseList(orderRepo.findByUserId(id));
    }

    public OrderResponseDTO saveOrder(OrderRequestDTO request) {
        Order order = orderMapper.toEntity(request);
        order.setId(0);
        order.setStatus(OrderStatus.PLASATA);
        order.setCreatedAt(null);

        if (Boolean.TRUE.equals(order.getTermsAccepted())) {
            order.setTermsAccepted(true);
            order.setTermsAcceptedAt(LocalDateTime.now());
            order.setTermsVersion(LocalDate.now().toString());
        }

        if (request.userId() != null) {
            int userId = request.userId();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            order.setUser(user);
        } else {
            order.setUser(null);
        }

        order.getProducts().forEach(op -> {
            if (op.getProduct() == null) {
                throw new RuntimeException("Comanda conține produs null.");
            }
            int productId = op.getProduct().getId();
            op.setProduct(productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId)));
            op.setOrder(order);
        });

        Order saved = orderRepo.save(order);

        emailService.sendOrderConfirmationEmail(saved);
        emailService.sendNewOrderNotificationToAdmins(saved);

        return orderMapper.toResponse(saved);
    }

    public OrderResponseDTO updateOrderStatus(int id, OrderStatus newStatus) {
        Order order = orderRepo.findById(id).orElseThrow();

        if (order.getStatus() == newStatus) {
            return orderMapper.toResponse(order);
        }

        order.setStatus(newStatus);
        Order saved = orderRepo.save(order);

        emailService.sendOrderStatusUpdateEmail(saved);

        return orderMapper.toResponse(saved);
    }

    public boolean hasUserPurchasedProduct(int userId, int productId) {
        List<Order> orders = orderRepo.findByUserId(userId);

        for (Order order : orders) {
            for (OrderProduct orderProduct : order.getProducts()) {
                if (orderProduct.getProduct().getId() == productId) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteOrder(int id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        orderRepo.delete(order);
    }
}
