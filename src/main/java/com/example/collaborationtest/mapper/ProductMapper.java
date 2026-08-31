package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.product.ProductRequestDTO;
import com.example.collaborationtest.dto.product.ProductResponseDTO;
import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Explicit, null-safe converter between {@link Product} and its DTOs.
 * <p>
 * Registered as a Spring {@code @Component} so it can be constructor-injected
 * into services. Resolving the parent {@link Categories} entity from
 * {@code categoryId} is intentionally left to the service layer (it needs a
 * repository lookup); this mapper only maps scalar fields and sets the resolved
 * category when handed one. Child collections are delegated to the respective
 * leaf mappers, each of which produces recursion-safe DTOs.
 */
@Component
public class ProductMapper {

    private final PlantMapper plantMapper;
    private final ImageMapper imageMapper;
    private final ReviewMapper reviewMapper;

    public ProductMapper(PlantMapper plantMapper, ImageMapper imageMapper, ReviewMapper reviewMapper) {
        this.plantMapper = plantMapper;
        this.imageMapper = imageMapper;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Builds a new {@link Product} from a request payload. Scalar fields only —
     * the caller is responsible for resolving and setting the {@link Categories}
     * association (and any child collections).
     */
    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .shortDesc(dto.shortDesc())
                .longDesc(dto.longDesc())
                .notification(dto.notification())
                .ingredients(dto.ingredients())
                .scientificStudies(dto.scientificStudies())
                .stockQty(dto.stockQty() != null ? dto.stockQty() : 0)
                .mainImageUrl(dto.mainImageUrl())
                .build();
    }

    /**
     * Copies mutable scalar fields from a request payload onto an existing,
     * managed {@link Product}. The category association is handled by the caller.
     */
    public void updateEntity(Product product, ProductRequestDTO dto) {
        if (product == null || dto == null) {
            return;
        }
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setShortDesc(dto.shortDesc());
        product.setLongDesc(dto.longDesc());
        product.setNotification(dto.notification());
        product.setIngredients(dto.ingredients());
        product.setScientificStudies(dto.scientificStudies());
        if (dto.stockQty() != null) {
            product.setStockQty(dto.stockQty());
        }
        product.setMainImageUrl(dto.mainImageUrl());
    }

    /**
     * Maps an entity to its read model. The parent category is flattened to
     * id + name; child collections become slim nested DTOs that never point
     * back to the product, so the serialized graph stays acyclic.
     */
    public ProductResponseDTO toResponse(Product product) {
        if (product == null) {
            return null;
        }

        Categories category = product.getCategories();
        Integer categoryId = category != null ? category.getId() : null;
        String categoryName = category != null ? category.getName() : null;

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getShortDesc(),
                product.getLongDesc(),
                product.getNotification(),
                product.getIngredients(),
                product.getScientificStudies(),
                product.getStockQty(),
                product.isActive(),
                product.getMainImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                categoryId,
                categoryName,
                plantMapper.toResponseList(product.getPlants()),
                imageMapper.toResponseList(product.getImages()),
                reviewMapper.toResponseList(product.getReviews())
        );
    }

    public List<ProductResponseDTO> toResponseList(List<Product> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
