package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.repository.projection.PopularProductView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderProductRepo  extends JpaRepository<OrderProduct, Integer> {

    /**
     * Aggregates the best-selling products by total units ordered, most sold first.
     * Limit the result set by passing a {@link Pageable} (e.g. {@code PageRequest.of(0, 5)}).
     */
    @Query("select p.name as name, sum(op.quantity) as totalSold " +
           "from OrderProduct op join op.product p " +
           "group by p.id, p.name " +
           "order by sum(op.quantity) desc")
    List<PopularProductView> findTopSellingProducts(Pageable pageable);

    /** Best-selling products for orders placed within a time range. */
    @Query("select p.name as name, sum(op.quantity) as totalSold " +
           "from OrderProduct op join op.product p join op.order o " +
           "where o.createdAt between :start and :end " +
           "group by p.id, p.name " +
           "order by sum(op.quantity) desc")
    List<PopularProductView> findTopSellingProductsBetween(@Param("start") LocalDateTime start,
                                                           @Param("end") LocalDateTime end,
                                                           Pageable pageable);
}
