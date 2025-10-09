package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Review;
import com.example.collaborationtest.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products/reviews")
public class ReviewController {
    public ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/getAll")
    public List<Review> getAll() {
        return reviewService.findAll();
    }

    @GetMapping("/getAllByProductId/{id}")
    public List<Review> getAllByProductId(@PathVariable int id) {
        return reviewService.findAllByProduct_Id(id);
    }

    @PostMapping("/add")
    public Review add(@RequestBody Review review) {
        review.setId(0);
        return reviewService.add(review);
    }
}
