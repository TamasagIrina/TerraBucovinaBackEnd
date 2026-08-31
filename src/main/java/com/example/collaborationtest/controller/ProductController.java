package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.common.PageResponse;
import com.example.collaborationtest.dto.product.ProductRequestDTO;
import com.example.collaborationtest.dto.product.ProductResponseDTO;
import com.example.collaborationtest.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return ResponseEntity.ok(productService.getProducts(includeInactive));
    }

    @GetMapping("/get/paged")
    public ResponseEntity<PageResponse<ProductResponseDTO>> getProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return ResponseEntity.ok(productService.getProductsPaged(PageRequest.of(page, size), includeInactive));
    }

    @GetMapping("/get/byId/{id}")
    public ResponseEntity<ProductResponseDTO> getProductsById(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/get/byName/{name}")
    public ResponseEntity<ProductResponseDTO> getProductByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @PostMapping("/admin/add")
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO request) {
        ProductResponseDTO created = productService.addProduct(request);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable int id,
                                                            @Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/reactivate/{id}")
    public ResponseEntity<ProductResponseDTO> reactivateProduct(@PathVariable int id) {
        return ResponseEntity.ok(productService.reactivateProduct(id));
    }
}
