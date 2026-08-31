package com.example.collaborationtest.dto.dashboard;

import java.math.BigDecimal;

/**
 * Overall system KPIs shown in the admin dashboard's summary cards.
 */
public record DashboardKpiResponseDTO(
        long totalProducts,
        long totalUsers,
        long totalOrders,
        long totalReviews,
        long pendingOrders,
        BigDecimal totalRevenue
) {
}
