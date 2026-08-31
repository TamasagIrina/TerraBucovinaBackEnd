package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.category.CategoriesRequestDTO;
import com.example.collaborationtest.dto.category.CategoriesResponseDTO;
import com.example.collaborationtest.mapper.CategoriesMapper;
import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.repository.CategoriesRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private CategoriesRepo categoriesRepo;
    private final CategoriesMapper categoriesMapper;

    public CategoriesService(CategoriesRepo categoriesRepo, CategoriesMapper categoriesMapper) {
        this.categoriesRepo = categoriesRepo;
        this.categoriesMapper = categoriesMapper;
    }

    public List<CategoriesResponseDTO> getAllCategories() {
        return categoriesMapper.toResponseList(categoriesRepo.findAll());
    }

    public CategoriesResponseDTO addCategories(CategoriesRequestDTO request) {
        Categories category = categoriesMapper.toEntity(request);
        return categoriesMapper.toResponse(categoriesRepo.save(category));
    }
}
