package com.example.collaborationtest.repository.projection;

/**
 * Spring Data interface projection for the "top selling products" aggregation:
 * a product name and the total units sold for it.
 */
public interface PopularProductView {
    String getName();
    Long getTotalSold();
}
