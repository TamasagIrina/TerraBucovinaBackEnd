package com.example.collaborationtest.repository;


import com.example.collaborationtest.model.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Integer> {

    @EntityGraph(attributePaths = "product")
    List<Review> findAllByProduct_Id(int productId);
}
