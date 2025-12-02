package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.repository.CategoriesRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private CategoriesRepo categoriesRepo;
    public CategoriesService(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }

   public List<Categories> getAllCategories() {
        return categoriesRepo.findAll();
   }

   public Categories addCategories(Categories categories) {
        categories.setId(0);
        return categoriesRepo.save(categories);
   }
}
