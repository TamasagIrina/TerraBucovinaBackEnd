package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.repository.OrderRepo;
import com.example.collaborationtest.repository.ProductRepo;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepo orderRepo;

    private OrderProductService orderProductService;

    private final ProductRepo productRepo;

    public OrderService(OrderRepo orderRepo, OrderProductService orderProductService, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.orderProductService = orderProductService;
        this.productRepo = productRepo;

    }

    public List<Order> getAllOrder() {
       return this.orderRepo.findAll();
    }

    public Order getOrderById(int id) {
        return this.orderRepo.findById(id).get();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return this.orderRepo.findAllByStatus(status);
    }


    public List<Order> getOrdersByCustomer(int id) {
        return this.orderRepo.findByUserId(id);
    }



    public Order saveOrder(Order order) {
        order.setId(null);
        order.setStatus(OrderStatus.PLASATA);
        order.setCreatedAt(null);

        order.getProducts().forEach(op -> {
            Integer productId = op.getProduct().getId();
            op.setProduct(productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId)));


            op.setOrder(order);
        });

        return orderRepo.save(order);
    }

    public Order updateOrderStatus(int id, OrderStatus status) {
        Order order = this.orderRepo.findById(id).get();
        order.setStatus(status);
        return this.orderRepo.save(order);

    }

    public Order deleteOrder(int id) {
        Order order = this.orderRepo.findById(id).get();
        this.orderRepo.delete(order);
        return order;
    }
}
