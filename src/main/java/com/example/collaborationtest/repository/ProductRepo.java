package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    Product findByName(String name);

    List<Product> findAllByActiveTrue();

    Page<Product> findAllByActiveTrue(Pageable pageable);
}
