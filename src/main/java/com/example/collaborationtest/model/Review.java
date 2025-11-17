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
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer productId;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;


    @Column(columnDefinition = "TEXT")
    private String body;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer stars;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;


}
