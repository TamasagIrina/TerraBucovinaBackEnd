package com.example.collaborationtest.repository;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo  extends JpaRepository<Order, Integer> {
    List<Order> findByUserId();

    List<Order> findAllByStatus(OrderStatus status);
}
