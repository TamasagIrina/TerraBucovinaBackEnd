package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Review;
import com.example.collaborationtest.repository.ReviewRepo;

import java.util.List;

public class ReviewService {

    private ReviewRepo reviewRepo;
    public ReviewService(ReviewRepo repo) {
        this.reviewRepo = repo;
    }

    public List<Review> findAllByProduct_Id(int productId) {
        if(reviewRepo.findAllByProduct_Id(productId).isEmpty()) {
            return null;
        }
        return reviewRepo.findAllByProduct_Id(productId);
    }

    public Review findById(int id) {
        return reviewRepo.findById(id).orElse(null);
    }
}
