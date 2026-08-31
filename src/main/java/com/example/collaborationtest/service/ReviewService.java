package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.review.ReviewRequestDTO;
import com.example.collaborationtest.dto.review.ReviewResponseDTO;
import com.example.collaborationtest.mapper.ReviewMapper;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.model.Review;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.dto.common.PageResponse;
import com.example.collaborationtest.repository.ProductRepo;
import com.example.collaborationtest.repository.ReviewRepo;
import com.example.collaborationtest.repository.UserRepo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    private ReviewRepo reviewRepo;
    private ProductRepo productRepo;
    private UserRepo userRepo;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepo repo, ProductRepo productRepo, UserRepo userRepo, ReviewMapper reviewMapper) {
        this.reviewRepo = repo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.reviewMapper = reviewMapper;
    }

    public List<ReviewResponseDTO> findAllByProduct_Id(int productId) {
        List<Review> reviews = reviewRepo.findAllByProduct_Id(productId);
        if (reviews.isEmpty()) {
            return null;
        }
        return reviewMapper.toResponseList(reviews);
    }

    public List<ReviewResponseDTO> findAll() {
        return reviewMapper.toResponseList(reviewRepo.findAll());
    }

    public PageResponse<ReviewResponseDTO> findAllPaged(Pageable pageable) {
        return PageResponse.from(reviewRepo.findAll(pageable).map(reviewMapper::toResponse));
    }

    public PageResponse<ReviewResponseDTO> findAllByProduct_IdPaged(int productId, Pageable pageable) {
        return PageResponse.from(reviewRepo.findAllByProduct_Id(productId, pageable).map(reviewMapper::toResponse));
    }

    public ReviewResponseDTO add(ReviewRequestDTO req) {
        Product product = productRepo.findById(req.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = Review.builder()
                .product(product)
                .user(user)
                .body(req.body())
                .stars(req.stars())
                .build();

        return reviewMapper.toResponse(reviewRepo.save(review));
    }

    private Review findOwnedReview(int id, int requesterUserId) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if (review.getUser() == null || review.getUser().getId() != requesterUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only manage your own reviews");
        }
        return review;
    }

    public ReviewResponseDTO update(int id, ReviewRequestDTO req, int requesterUserId) {
        Review review = findOwnedReview(id, requesterUserId);
        review.setBody(req.body());
        review.setStars(req.stars());
        return reviewMapper.toResponse(reviewRepo.save(review));
    }

    public void delete(int id, int requesterUserId) {
        Review review = findOwnedReview(id, requesterUserId);
        reviewRepo.delete(review);
    }
}
