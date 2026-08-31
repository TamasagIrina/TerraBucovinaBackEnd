package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.review.ReviewResponseDTO;
import com.example.collaborationtest.model.Review;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts {@link Review} entities to their read model. The product and user
 * associations are resolved in the service (they require repository lookups),
 * so this mapper only produces the response view.
 */
@Component
public class ReviewMapper {

    public ReviewResponseDTO toResponse(Review review) {
        if (review == null) {
            return null;
        }
        Integer productId = review.getProduct() != null
                ? review.getProduct().getId()
                : review.getProductId();
        Integer userId = review.getUser() != null
                ? review.getUser().getId()
                : review.getUserId();
        return new ReviewResponseDTO(
                review.getId(),
                productId,
                userId,
                review.getBody(),
                review.getStars(),
                review.getCreatedAt()
        );
    }

    public List<ReviewResponseDTO> toResponseList(List<Review> reviews) {
        return Optional.ofNullable(reviews).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
