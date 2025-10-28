package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.service.EmailService;
import com.example.collaborationtest.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    private final EmailService emailService;

    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @GetMapping("/get/all")
    public List<Order> getAll(){
        return orderService.getAllOrder();
    }

    @GetMapping("/get/byUserId/{id}")
    public List<Order> getOrderById(@PathVariable int id){
        return orderService.getOrdersByCustomer(id);
    }


    @PostMapping("/add")
    public Order addOrder(@RequestBody Order order){

        Order newOrder = orderService.saveOrder(order);

        if(newOrder==null){
            return null;
        }

        emailService.sendOrderConfirmationEmail(newOrder);

        return newOrder;
    }

    @PutMapping("/updateStatus/{id}/{orderStatur}")
    public Order updateStatus(@PathVariable int id, @PathVariable OrderStatus orderStatur){
            return orderService.updateOrderStatus(id,orderStatur);
    }
}
