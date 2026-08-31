package com.example.collaborationtest.dto.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Lightweight, stable pagination envelope returned to clients — a slim
 * alternative to serializing Spring's {@code Page} directly.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
