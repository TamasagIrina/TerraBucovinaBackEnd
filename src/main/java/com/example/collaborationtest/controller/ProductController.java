package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @GetMapping("/getAll")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/getById")
    public Product getProductsById(@RequestParam int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/getByName")
    public Product getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }


}
