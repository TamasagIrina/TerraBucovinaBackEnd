package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.plant.PlantRequestDTO;
import com.example.collaborationtest.dto.plant.PlantResponseDTO;
import com.example.collaborationtest.model.Plant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts between {@link Plant} and its DTOs. The owning {@code Product}
 * association is resolved and set by the service layer.
 */
@Component
public class PlantMapper {

    public Plant toEntity(PlantRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Plant.builder()
                .name(dto.name())
                .shortDescription(dto.shortDescription())
                .longDescription(dto.longDescription())
                .plantMessage(dto.plantMessage())
                .build();
    }

    public PlantResponseDTO toResponse(Plant plant) {
        if (plant == null) {
            return null;
        }
        Integer productId = plant.getProduct() != null ? plant.getProduct().getId() : null;
        return new PlantResponseDTO(
                plant.getId(),
                productId,
                plant.getName(),
                plant.getImageUrl(),
                plant.getShortDescription(),
                plant.getLongDescription(),
                plant.getPlantMessage()
        );
    }

    public List<PlantResponseDTO> toResponseList(List<Plant> plants) {
        return Optional.ofNullable(plants).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
