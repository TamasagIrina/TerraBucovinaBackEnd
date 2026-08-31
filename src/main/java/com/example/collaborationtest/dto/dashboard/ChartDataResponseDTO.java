package com.example.collaborationtest.dto.dashboard;

import java.util.List;

/**
 * Generic chart payload: parallel {@code labels} and {@code values} arrays,
 * ready to feed a bar/pie chart on the frontend (e.g. product names vs units sold).
 */
public record ChartDataResponseDTO(
        List<String> labels,
        List<Long> values
) {
}
