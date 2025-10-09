package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    Product findByName(String name);
}
