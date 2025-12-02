package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.service.CategoriesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private CategoriesService categoriesService;
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/get/all")
    public List<Categories> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    @PostMapping("add")
    public Categories addCategories(@RequestBody Categories categories) {
        return categoriesService.addCategories(categories);
    }
}
