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

    public List<Review> findAll(){
        return reviewRepo.findAll();
    }

    public Review findById(int id) {
        return reviewRepo.findById(id).orElse(null);
    }

    public Review add (Review review) {
        if (review.getId() == 0) {
            return reviewRepo.save(review);
        }
        return null;
    }
}
