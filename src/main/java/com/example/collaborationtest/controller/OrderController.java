package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.service.EmailService;
import com.example.collaborationtest.service.OrderService;
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
    public ResponseEntity<Map<String, Object>>  addOrder(@RequestBody Order order){

        Order newOrder = orderService.saveOrder(order);

        Map<String, Object> response = new HashMap<>();

        if(newOrder==null){
            response.put("success", false);
            response.put("message", "Comanda nu a putut fi înregistrată, încercați din nou!");
        }

        emailService.sendOrderConfirmationEmail(newOrder);

        response.put("success", true);
        response.put("message", "Comanda a fost trimisă cu succes!");
        response.put("order", newOrder);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateStatus/{id}/{orderStatus}")
    public Order updateStatus(@PathVariable int id, @PathVariable OrderStatus orderStatus){
            return orderService.updateOrderStatus(id,orderStatus);
    }
}
