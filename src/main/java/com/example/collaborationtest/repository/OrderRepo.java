package com.example.collaborationtest.repository;

import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepo  extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int id);

    Page<Order> findByUserId(int id, Pageable pageable);

    List<Order> findAllByStatus(OrderStatus status);

    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    long countByStatus(OrderStatus status);

    /** Sum of every order's total price (0 when there are no orders). */
    @Query("select coalesce(sum(o.totalPrice), 0) from Order o")
    BigDecimal sumTotalRevenue();

    /** Orders placed within a time range (used for the monthly summary). */
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("select coalesce(sum(o.totalPrice), 0) from Order o where o.createdAt between :start and :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
