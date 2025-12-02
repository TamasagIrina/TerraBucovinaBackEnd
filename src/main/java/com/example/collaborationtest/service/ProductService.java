package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Categories;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.CategoriesRepo;
import com.example.collaborationtest.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoriesRepo categoriesRepo;
    public ProductService(ProductRepo productRepo, CategoriesRepo categoriesRepo) {
        this.productRepo = productRepo;
        this.categoriesRepo = categoriesRepo;
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

        if (getProductByName(product.getName()) != null) {
            return null;
        }


        Integer categoryId = product.getCategories() != null ? product.getCategories().getId() : null;

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID is required!");
        }


        Categories category = categoriesRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));


        product.setCategories(category);

        return productRepo.save(product);
    }

    public Product updateProduct(Product product) {
        Product product1= getProductById(product.getId());
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setUpdatedAt(product.getUpdatedAt());
        product1.setLongDesc(product.getLongDesc());
        product1.setShortDesc(product.getShortDesc());

        return productRepo.save(product1);

    }

    public Product deleteProduct(int id) {
        Product product1= getProductById(id);
        productRepo.delete(product1);
        return product1;
    }
}
