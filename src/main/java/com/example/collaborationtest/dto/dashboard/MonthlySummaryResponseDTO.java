package com.example.collaborationtest.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

/**
 * Aggregated recap of a single month, emailed to admins by the scheduler.
 */
public record MonthlySummaryResponseDTO(
        String monthLabel,
        long totalOrders,
        BigDecimal totalRevenue,
        long totalProducts,
        long totalUsers,
        List<TopProduct> topProducts
) {
    /** A single best-selling product line for the month. */
    public record TopProduct(String name, long quantity) {
    }
}
