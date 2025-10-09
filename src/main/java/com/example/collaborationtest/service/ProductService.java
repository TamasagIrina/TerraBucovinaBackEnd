package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.ProductRepo;

import java.util.List;

public class ProductService {

    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id).get();
    }

    public Product getProductByName(String name) {
        return productRepo.findByName(name);
    }
    public Product addProduct(Product product) {

        if(getProductByName(product.getName()) != null) {
            return null;
        }

        return productRepo.save(product);
    }
}
