package com.example.collaborationtest.scheduler;

import com.example.collaborationtest.dto.dashboard.MonthlySummaryResponseDTO;
import com.example.collaborationtest.service.AdminDashboardService;
import com.example.collaborationtest.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Emails a monthly recap to all admins. Runs on the 28th of each month by
 * default (28 is safe for every month, February included). The cron and zone
 * are configurable via {@code app.monthly-summary.cron}.
 */
@Component
public class MonthlySummaryScheduler {

    private static final String[] MONTHS_RO = {
            "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
            "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"
    };

    private final AdminDashboardService dashboardService;
    private final EmailService emailService;

    public MonthlySummaryScheduler(AdminDashboardService dashboardService, EmailService emailService) {
        this.dashboardService = dashboardService;
        this.emailService = emailService;
    }

    // second minute hour day-of-month month day-of-week — 09:00 on the 28th, every month.
    @Scheduled(cron = "${app.monthly-summary.cron:0 0 9 28 * *}", zone = "Europe/Bucharest")
    public void sendMonthlySummary() {
        runForCurrentMonth();
    }

    /**
     * Computes the current-month recap and mails it. Exposed so it can also be
     * triggered manually (e.g. from an admin endpoint for testing).
     */
    public void runForCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        String monthLabel = MONTHS_RO[today.getMonthValue() - 1] + " " + today.getYear();

        MonthlySummaryResponseDTO summary = dashboardService.getMonthlySummary(start, end, monthLabel);
        emailService.sendMonthlySummaryToAdmins(summary);
    }
}
