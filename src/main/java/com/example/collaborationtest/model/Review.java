package com.example.collaborationtest.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("product-review")
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference("user-review")
    private User user;


    @Column(columnDefinition = "TEXT")
    private String body;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer stars;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;


}
