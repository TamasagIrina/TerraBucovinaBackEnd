package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.common.PageResponse;
import com.example.collaborationtest.dto.order.OrderRequestDTO;
import com.example.collaborationtest.dto.order.OrderResponseDTO;
import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<OrderResponseDTO>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @GetMapping("/get/all/paged")
    public ResponseEntity<PageResponse<OrderResponseDTO>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getAllOrderPaged(status, PageRequest.of(page, size)));
    }

    @GetMapping("/get/byUserId/{id}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(id));
    }

    @GetMapping("/get/byUserId/{id}/paged")
    public ResponseEntity<PageResponse<OrderResponseDTO>> getOrderByIdPaged(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerPaged(id, PageRequest.of(page, size)));
    }

    @PostMapping("/add")
    public ResponseEntity<OrderResponseDTO> addOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO newOrder = orderService.saveOrder(request);

        if (newOrder == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @PutMapping("/updateStatus/{id}/{orderStatus}")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable int id,
                                                         @PathVariable OrderStatus orderStatus) {
        OrderResponseDTO order = orderService.updateOrderStatus(id, orderStatus);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}
