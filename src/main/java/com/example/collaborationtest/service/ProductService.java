package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
