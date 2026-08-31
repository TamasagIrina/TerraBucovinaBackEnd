package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.user.UserRequestDTO;
import com.example.collaborationtest.dto.user.UserResponseDTO;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.Review;
import com.example.collaborationtest.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts between {@link User} and its DTOs.
 * <p>
 * {@link #toEntity} carries the <em>raw</em> password straight from the request;
 * hashing is the service's responsibility. {@link #toResponse} <strong>never</strong>
 * copies the password out, and reduces the user's orders/reviews to id lists.
 */
@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setTermsAccepted(dto.termsAccepted());
        return user;
    }

    public UserResponseDTO toResponse(User user) {
        if (user == null) {
            return null;
        }
        List<Integer> orderIds = Optional.ofNullable(user.getOrders()).orElseGet(List::of)
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        List<Integer> reviewIds = Optional.ofNullable(user.getReviews()).orElseGet(List::of)
                .stream()
                .map(Review::getId)
                .collect(Collectors.toList());
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAddress(),
                user.getRoles(),
                user.isEnabled(),
                user.isTermsAccepted(),
                user.getTermsAcceptedAt(),
                user.getTermsVersion(),
                orderIds,
                reviewIds
        );
    }

    public List<UserResponseDTO> toResponseList(List<User> users) {
        return Optional.ofNullable(users).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
