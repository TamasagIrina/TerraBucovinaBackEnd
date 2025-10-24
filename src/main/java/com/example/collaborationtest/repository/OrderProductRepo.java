package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepo  extends JpaRepository<OrderProduct, Integer> {
}
