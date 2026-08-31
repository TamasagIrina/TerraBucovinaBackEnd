package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.plant.PlantRequestDTO;
import com.example.collaborationtest.dto.plant.PlantResponseDTO;
import com.example.collaborationtest.mapper.PlantMapper;
import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.PlantRepo;
import com.example.collaborationtest.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PlantService {
    public PlantRepo plantRepo;
    public ProductRepo productRepo;
    private final PlantMapper plantMapper;
    private final R2StorageService storage;

    public PlantService(PlantRepo plantRepo, ProductRepo productRepo, PlantMapper plantMapper,
                        R2StorageService storage) {
        this.plantRepo = plantRepo;
        this.productRepo = productRepo;
        this.plantMapper = plantMapper;
        this.storage = storage;
    }

    public PlantResponseDTO findPlantById(int id) {
        return plantMapper.toResponse(plantRepo.findById(id).orElse(null));
    }

    public List<PlantResponseDTO> findAllPlants() {
        return plantMapper.toResponseList(plantRepo.findAll());
    }

    public List<PlantResponseDTO> findPlantProductById(int id) {
        List<Plant> plants = plantRepo.findAllByProduct_Id(id);
        if (plants.isEmpty()) {
            return null;
        }
        return plantMapper.toResponseList(plants);
    }

    /**
     * Creates a plant from its request payload and stored image. The product is
     * resolved from the database, the entity persisted to obtain an id, then the
     * uploaded file is stored under that id and the resulting URL saved.
     */
    public PlantResponseDTO addPlant(PlantRequestDTO request, MultipartFile file) throws IOException {
        Product product = productRepo.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.productId()));

        Plant plant = plantMapper.toEntity(request);
        plant.setProduct(product);
        Plant saved = plantRepo.save(plant);

        var stored = storage.saveProductImage(saved.getId(), file);
        saved.setImageUrl(stored.publicUrl());

        return plantMapper.toResponse(plantRepo.save(saved));
    }

    public void deletePlant(int id) {
        Plant oldPlant = plantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found: " + id));
        storage.deleteByPublicUrl(oldPlant.getImageUrl());
        plantRepo.delete(oldPlant);
    }
}
