package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.repository.OrderRepo;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepo orderRepo;

    private OrderProductService orderProductService;

    public OrderService(OrderRepo orderRepo, OrderProductService orderProductService) {
        this.orderRepo = orderRepo;
        this.orderProductService = orderProductService;

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
        if (order.getProducts() != null) {
            for (OrderProduct product : order.getProducts()) {
                product.setOrder(order);
            }
        }
        return this.orderRepo.save(order);
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
