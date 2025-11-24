package com.example.collaborationtest.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // (Many) -> (One) Product
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(nullable = false, length = 200)
    private String name;

    @Column( length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(length = 1000)
    private String longDescription;

    @Column(length = 300)
    private String plantMessage;
}
