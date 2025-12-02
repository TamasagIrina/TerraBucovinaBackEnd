package com.example.collaborationtest.repository;
import com.example.collaborationtest.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepo extends JpaRepository<Categories, Integer> {
}
