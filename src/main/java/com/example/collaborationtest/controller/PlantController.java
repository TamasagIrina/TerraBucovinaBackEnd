package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.model.PlantDTO;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.ProductRepo;
import com.example.collaborationtest.service.FileSystemStorageService;
import com.example.collaborationtest.service.PlantService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/products/plants")
public class PlantController {

    private PlantService plantService;
    private final FileSystemStorageService storage;
    private ProductRepo productRepo;

    public PlantController(PlantService plantService, FileSystemStorageService storage, ProductRepo productRepo) {
        this.plantService = plantService;
        this.storage = storage;
        this.productRepo = productRepo;
    }

    @GetMapping("/getAll")
    public List<Plant> getAll() {
       return this.plantService.findAllPlants();
    }

    @GetMapping("/getById/{id}")
    public Plant getById(@RequestParam int id) {
        return this.plantService.findPlantById(id);
    }

    @GetMapping("/getByProductId/{id}")
    public List<Plant> getByProductId(@RequestParam int productId) {
        return this.plantService.findPlantProductById(productId);
    }

    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Plant addPlant(
            @RequestPart("plant") PlantDTO dto,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Plant plant = new Plant();
            plant.setId(0);
            plant.setName(dto.getName());
            plant.setShortDescription(dto.getShortDescription());
            plant.setLongDescription(dto.getLongDescription());
            plant.setPlantMessage(dto.getPlantMessage());
            plant.setProduct(product);
            plant.setId(0);
        Plant saved = plantService.addPlant(plant);
        var stored = storage.saveProductImage(saved.getId(), file);
        plant.setImageUrl(stored.publicUrl());

        return plantService.updatePlant(saved);
    }



    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        plantService.deletePlant(id);
    }
}
