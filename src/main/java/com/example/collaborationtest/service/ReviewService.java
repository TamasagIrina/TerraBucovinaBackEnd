package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.model.Review;
import com.example.collaborationtest.model.ReviewRequest;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.ProductRepo;
import com.example.collaborationtest.repository.ReviewRepo;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private ReviewRepo reviewRepo;

    private ProductRepo productRepo;

    private UserRepo userRepo;

    public ReviewService(ReviewRepo repo, ProductRepo productRepo, UserRepo userRepo) {
        this.reviewRepo = repo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public List<Review> findAllByProduct_Id(int productId) {
        if(reviewRepo.findAllByProduct_Id(productId).isEmpty()) {
            return null;
        }
        return reviewRepo.findAllByProduct_Id(productId);
    }

    public List<Review> findAll(){
        return reviewRepo.findAll();
    }

    public Review findById(int id) {
        return reviewRepo.findById(id).orElse(null);
    }

    public Review add(ReviewRequest req) {

        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = Review.builder()
                .product(product)
                .user(user)
                .body(req.getBody())
                .stars(req.getStars())
                .build();

        return reviewRepo.save(review);
    }
}
