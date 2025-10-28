package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepo  extends JpaRepository<OrderProduct, Integer> {
}
