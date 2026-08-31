package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.dashboard.ChartDataResponseDTO;
import com.example.collaborationtest.dto.dashboard.DashboardKpiResponseDTO;
import com.example.collaborationtest.scheduler.MonthlySummaryScheduler;
import com.example.collaborationtest.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin-only analytics endpoints. Secured via SecurityConfig ({@code /api/admin/**}
 * requires ROLE_ADMIN).
 */
@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;
    private final MonthlySummaryScheduler monthlySummaryScheduler;

    public AdminDashboardController(AdminDashboardService dashboardService,
                                    MonthlySummaryScheduler monthlySummaryScheduler) {
        this.dashboardService = dashboardService;
        this.monthlySummaryScheduler = monthlySummaryScheduler;
    }

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKpiResponseDTO> getKpis() {
        return ResponseEntity.ok(dashboardService.getKpis());
    }

    @GetMapping("/popular-products")
    public ResponseEntity<ChartDataResponseDTO> getPopularProducts(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dashboardService.getPopularProducts(limit));
    }

    /**
     * Manually triggers the monthly summary email to admins (useful for testing
     * without waiting for the scheduled run).
     */
    @PostMapping("/send-monthly-summary")
    public ResponseEntity<String> sendMonthlySummaryNow() {
        monthlySummaryScheduler.runForCurrentMonth();
        return ResponseEntity.ok("Rezumatul lunar a fost trimis administratorilor.");
    }
}
