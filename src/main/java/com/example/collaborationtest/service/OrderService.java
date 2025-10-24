package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.repository.OrderRepo;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
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
        return this.orderRepo.findByUserId();
    }

    public Order saveOrder(Order order) {
        return this.orderRepo.save(order);
    }

    public Order updateOrderStatus(int id, OrderStatus status) {
        Order order = this.orderRepo.findById(id).get();
        order.setStatus(status);
        return this.orderRepo.save(order);

    }
}
