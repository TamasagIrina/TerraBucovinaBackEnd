package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.dashboard.ChartDataResponseDTO;
import com.example.collaborationtest.dto.dashboard.DashboardKpiResponseDTO;
import com.example.collaborationtest.dto.dashboard.MonthlySummaryResponseDTO;
import com.example.collaborationtest.enums.OrderStatus;
import com.example.collaborationtest.repository.OrderProductRepo;
import com.example.collaborationtest.repository.OrderRepo;
import com.example.collaborationtest.repository.ProductRepo;
import com.example.collaborationtest.repository.ReviewRepo;
import com.example.collaborationtest.repository.UserRepo;
import com.example.collaborationtest.repository.projection.PopularProductView;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Read-only aggregation service backing the admin dashboard. It only counts and
 * sums existing data through the repositories — no entities are exposed.
 */
@Service
public class AdminDashboardService {

    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final ReviewRepo reviewRepo;
    private final OrderProductRepo orderProductRepo;

    public AdminDashboardService(ProductRepo productRepo, UserRepo userRepo, OrderRepo orderRepo,
                                 ReviewRepo reviewRepo, OrderProductRepo orderProductRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.reviewRepo = reviewRepo;
        this.orderProductRepo = orderProductRepo;
    }

    public DashboardKpiResponseDTO getKpis() {
        return new DashboardKpiResponseDTO(
                productRepo.count(),
                userRepo.count(),
                orderRepo.count(),
                reviewRepo.count(),
                orderRepo.countByStatus(OrderStatus.PLASATA),
                orderRepo.sumTotalRevenue()
        );
    }

    /**
     * Top-selling products as chart-ready labels/values, capped at {@code limit}.
     */
    public ChartDataResponseDTO getPopularProducts(int limit) {
        int safeLimit = limit > 0 ? limit : 5;
        List<PopularProductView> top = orderProductRepo.findTopSellingProducts(PageRequest.of(0, safeLimit));

        List<String> labels = top.stream().map(PopularProductView::getName).toList();
        List<Long> values = top.stream()
                .map(v -> v.getTotalSold() != null ? v.getTotalSold() : 0L)
                .toList();

        return new ChartDataResponseDTO(labels, values);
    }

    /**
     * Builds the recap for a month (orders + revenue in the range, current
     * catalog/user totals, and the month's top-selling products).
     */
    public MonthlySummaryResponseDTO getMonthlySummary(LocalDateTime start, LocalDateTime end, String monthLabel) {
        long monthlyOrders = orderRepo.countByCreatedAtBetween(start, end);
        var revenue = orderRepo.sumRevenueBetween(start, end);

        List<MonthlySummaryResponseDTO.TopProduct> topProducts =
                orderProductRepo.findTopSellingProductsBetween(start, end, PageRequest.of(0, 5))
                        .stream()
                        .map(v -> new MonthlySummaryResponseDTO.TopProduct(
                                v.getName(),
                                v.getTotalSold() != null ? v.getTotalSold() : 0L))
                        .toList();

        return new MonthlySummaryResponseDTO(
                monthLabel,
                monthlyOrders,
                revenue,
                productRepo.count(),
                userRepo.count(),
                topProducts
        );
    }
}
