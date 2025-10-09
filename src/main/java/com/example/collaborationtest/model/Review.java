package com.example.collaborationtest.model;


import com.example.collaborationtest.enums.ReviewStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_review_user_product", columnNames = {"user_id","product_id"})
        })
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // (Many) -> (One) Product
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Product product;

    // (Many) -> (One) User (clasa ta existentă)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private User user;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer stars;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReviewStatus status = ReviewStatus.APPROVED;
}
