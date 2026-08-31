package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.category.CategoriesRequestDTO;
import com.example.collaborationtest.dto.category.CategoriesResponseDTO;
import com.example.collaborationtest.service.CategoriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CategoriesResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @PostMapping("add")
    public ResponseEntity<CategoriesResponseDTO> addCategories(@Valid @RequestBody CategoriesRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesService.addCategories(request));
    }
}
