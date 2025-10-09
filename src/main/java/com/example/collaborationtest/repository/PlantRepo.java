package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.Plant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepo extends JpaRepository<Plant, Integer> {
    @EntityGraph(attributePaths = "product")
    List<Plant> findAllByProduct_Id(int productId);
}
