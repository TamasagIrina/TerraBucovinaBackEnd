package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.image.ImageResponseDTO;
import com.example.collaborationtest.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts {@link Image} entities to their read model. Creation is driven by a
 * multipart upload handled in the service, so no {@code toEntity} is needed here.
 */
@Component
public class ImageMapper {

    public ImageResponseDTO toResponse(Image image) {
        if (image == null) {
            return null;
        }
        Integer productId = image.getProduct() != null
                ? image.getProduct().getId()
                : image.getProductId();
        return new ImageResponseDTO(
                image.getId(),
                productId,
                image.getImageUrl(),
                image.getAltText(),
                image.getSortOrder(),
                image.getIsPrimary()
        );
    }

    public List<ImageResponseDTO> toResponseList(List<Image> images) {
        return Optional.ofNullable(images).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
