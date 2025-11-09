package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.OrderRepo;
import com.example.collaborationtest.repository.ProductRepo;
import com.example.collaborationtest.repository.UserRepo;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepo orderRepo;

    private OrderProductService orderProductService;

    private final ProductRepo productRepo;

    private final UserRepo userRepo;

    public OrderService(OrderRepo orderRepo, OrderProductService orderProductService, ProductRepo productRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.orderProductService = orderProductService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;

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



        if (order.getUser() != null) {
            int userId = order.getUser().getId();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            order.setUser(user);
        } else {
            order.setUser(null);
        }

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



    public Order deleteOrder(int id) {
        Order order = this.orderRepo.findById(id).get();
        this.orderRepo.delete(order);
        return order;
    }
}
