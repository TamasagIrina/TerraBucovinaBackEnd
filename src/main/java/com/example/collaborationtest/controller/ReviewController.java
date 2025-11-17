package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Review;
import com.example.collaborationtest.model.ReviewRequest;
import com.example.collaborationtest.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/products/reviews")
public class ReviewController {
    public ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/get/all")
    public List<Review> getAll() {
        return reviewService.findAll();
    }

    @GetMapping("/get/allByProductId/{id}")
    public List<Review> getAllByProductId(@PathVariable int id) {
        return reviewService.findAllByProduct_Id(id);
    }

    @PostMapping("/add")
    public Review add(@RequestBody ReviewRequest request) {
        return reviewService.add(request);
    }
}
