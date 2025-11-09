package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.service.EmailService;
import com.example.collaborationtest.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Order> addOrder(@RequestBody Order order){

        Order newOrder = orderService.saveOrder(order);

        if(newOrder==null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        emailService.sendOrderConfirmationEmail(newOrder);

        return ResponseEntity.ok(newOrder);
    }

    @PutMapping("/updateStatus/{id}/{orderStatus}")
    public Order updateStatus(@PathVariable int id, @PathVariable OrderStatus orderStatus){
            return orderService.updateOrderStatus(id,orderStatus);
    }

    @GetMapping("/can-review/{userId}/{productId}")
    public boolean canUserReview(@PathVariable int userId, @PathVariable int productId) {
        return orderService.hasUserPurchasedProduct(userId, productId);
    }
}
