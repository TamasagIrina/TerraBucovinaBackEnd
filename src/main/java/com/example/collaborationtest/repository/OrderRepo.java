package com.example.collaborationtest.repository;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo  extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int id);

    List<Order> findAllByStatus(OrderStatus status);
}
