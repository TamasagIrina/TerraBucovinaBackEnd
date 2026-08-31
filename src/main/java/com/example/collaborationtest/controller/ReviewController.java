package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.common.PageResponse;
import com.example.collaborationtest.dto.review.ReviewRequestDTO;
import com.example.collaborationtest.dto.review.ReviewResponseDTO;
import com.example.collaborationtest.service.ReviewService;
import com.example.collaborationtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/reviews")
public class ReviewController {
    public ReviewService reviewService;
    public UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ReviewResponseDTO>> getAll() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/get/allByProductId/{id}")
    public ResponseEntity<List<ReviewResponseDTO>> getAllByProductId(@PathVariable int id) {
        return ResponseEntity.ok(reviewService.findAllByProduct_Id(id));
    }

    @GetMapping("/get/all/paged")
    public ResponseEntity<PageResponse<ReviewResponseDTO>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.findAllPaged(PageRequest.of(page, size)));
    }

    @GetMapping("/get/allByProductId/{id}/paged")
    public ResponseEntity<PageResponse<ReviewResponseDTO>> getAllByProductIdPaged(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.findAllByProduct_IdPaged(id, PageRequest.of(page, size)));
    }

    @PostMapping("/add")
    public ResponseEntity<ReviewResponseDTO> add(@Valid @RequestBody ReviewRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.add(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable int id,
                                                     @Valid @RequestBody ReviewRequestDTO request,
                                                     Authentication authentication) {
        int requesterUserId = userService.getIdByEmail(authentication.getName());
        return ResponseEntity.ok(reviewService.update(id, request, requesterUserId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id, Authentication authentication) {
        int requesterUserId = userService.getIdByEmail(authentication.getName());
        reviewService.delete(id, requesterUserId);
        return ResponseEntity.noContent().build();
    }
}
