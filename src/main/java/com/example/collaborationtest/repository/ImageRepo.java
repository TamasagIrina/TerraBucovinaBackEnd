package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.Image;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepo extends JpaRepository<Image, Integer> {

    @EntityGraph(attributePaths = "product")
    List<Image> findAllByProduct_Id(int productId);

}
