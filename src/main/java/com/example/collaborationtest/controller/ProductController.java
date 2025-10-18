package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.service.ProductService;
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
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/get/byId/{id}")
    public Product getProductsById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/get/byName/{name}")
    public Product getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @PostMapping("/admin/add")
    public Product addProduct(@RequestBody Product product) {
        product.setId(0);
        return productService.addProduct(product);
    }

    @PutMapping("admin/update")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("admin/delete/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }


}
