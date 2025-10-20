package com.example.collaborationtest.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.awt.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "name",nullable = false, length = 200)
    private String name;

    @Column(name = "price",nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "short_desc", length = 500)
    private String shortDesc;

    @Column(name = "long_desc", length = 1000)
    private String longDesc;

    @Column(length = 100)
    private String category;

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 0;

    @Column(name = "main_image_url", length = 500)
    private String mainImageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private String updatedAt;

    // --- RELAȚII ---
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Plant> plants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
}
