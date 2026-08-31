package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.category.CategoriesRequestDTO;
import com.example.collaborationtest.dto.category.CategoriesResponseDTO;
import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts between {@link Categories} and its DTOs. Related products are
 * reduced to an id list to keep responses lean and recursion-free.
 */
@Component
public class CategoriesMapper {

    public Categories toEntity(CategoriesRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Categories.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
    }

    public void updateEntity(Categories category, CategoriesRequestDTO dto) {
        if (category == null || dto == null) {
            return;
        }
        category.setName(dto.name());
        category.setDescription(dto.description());
    }

    public CategoriesResponseDTO toResponse(Categories category) {
        if (category == null) {
            return null;
        }
        List<Integer> productIds = Optional.ofNullable(category.getProducts()).orElseGet(List::of)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        return new CategoriesResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                productIds
        );
    }

    public List<CategoriesResponseDTO> toResponseList(List<Categories> categories) {
        return Optional.ofNullable(categories).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
