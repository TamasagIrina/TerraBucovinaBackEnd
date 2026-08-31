package com.example.collaborationtest.dto.user;

import com.example.collaborationtest.enums.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Read model for a {@code User}.
 * <p>
 * <strong>The password is deliberately absent</strong> and must never be added.
 * Orders and reviews are exposed as id lists to avoid leaking nested graphs
 * (and to keep the user response from dragging in order/review details).
 */
public record UserResponseDTO(
        int id,
        String username,
        String email,
        String fullName,
        String address,
        Set<Role> roles,
        boolean enabled,
        boolean termsAccepted,
        LocalDateTime termsAcceptedAt,
        String termsVersion,
        List<Integer> orderIds,
        List<Integer> reviewIds
) {
}
