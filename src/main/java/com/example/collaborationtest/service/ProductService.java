package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.product.ProductRequestDTO;
import com.example.collaborationtest.dto.product.ProductResponseDTO;
import com.example.collaborationtest.mapper.ProductMapper;
import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.dto.common.PageResponse;
import com.example.collaborationtest.repository.CategoriesRepo;
import com.example.collaborationtest.repository.ProductRepo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoriesRepo categoriesRepo;
    private final ProductMapper productMapper;

    public ProductService(ProductRepo productRepo, CategoriesRepo categoriesRepo, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.categoriesRepo = categoriesRepo;
        this.productMapper = productMapper;
    }

    public List<ProductResponseDTO> getProducts(boolean includeInactive) {
        List<Product> products = includeInactive ? productRepo.findAll() : productRepo.findAllByActiveTrue();
        return productMapper.toResponseList(products);
    }

    public PageResponse<ProductResponseDTO> getProductsPaged(Pageable pageable, boolean includeInactive) {
        var page = includeInactive ? productRepo.findAll(pageable) : productRepo.findAllByActiveTrue(pageable);
        return PageResponse.from(page.map(productMapper::toResponse));
    }

    public ProductResponseDTO getProductById(int id) {
        return productMapper.toResponse(findEntityById(id));
    }

    public ProductResponseDTO getProductByName(String name) {
        return productMapper.toResponse(productRepo.findByName(name));
    }

    public ProductResponseDTO addProduct(ProductRequestDTO request) {
        if (productRepo.findByName(request.name()) != null) {
            return null;
        }

        Categories category = categoriesRepo.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.categoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategories(category);

        return productMapper.toResponse(productRepo.save(product));
    }

    public ProductResponseDTO updateProduct(int id, ProductRequestDTO request) {
        Product existing = findEntityById(id);
        productMapper.updateEntity(existing, request);

        if (request.categoryId() != null) {
            Categories category = categoriesRepo.findById(request.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + request.categoryId()));
            existing.setCategories(category);
        }

        return productMapper.toResponse(productRepo.save(existing));
    }

    /**
     * Soft delete: a product that's already been ordered can't be hard-deleted
     * (FK from order_products), and even for one that hasn't we want order
     * history to keep working if it ever is ordered later. Deactivating just
     * hides it from customer-facing listings; {@link #reactivateProduct} undoes it.
     */
    public void deleteProduct(int id) {
        Product product = findEntityById(id);
        product.setActive(false);
        productRepo.save(product);
    }

    public ProductResponseDTO reactivateProduct(int id) {
        Product product = findEntityById(id);
        product.setActive(true);
        return productMapper.toResponse(productRepo.save(product));
    }

    /**
     * Internal lookup returning the managed entity. Kept package-visible so
     * collaborating services (e.g. email) can obtain the entity when needed,
     * while controllers only ever see DTOs.
     */
    Product findEntityById(int id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }
}
