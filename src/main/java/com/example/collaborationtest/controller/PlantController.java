package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.service.FileSystemStorageService;
import com.example.collaborationtest.service.PlantService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/products/plants")
public class PlantController {

    private PlantService plantService;
    private final FileSystemStorageService storage;

    public PlantController(PlantService plantService, FileSystemStorageService storage) {
        this.plantService = plantService;
        this.storage = storage;
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

    @PostMapping("/add")
    public Plant addPlant(
            @RequestBody Plant plant,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        var stored = storage.saveProductImage(plant.getId(), file);
        plant.setId(0);
        plant.setImageUrl(stored.publicUrl());
        return this.plantService.addPlant(plant);
    }

    @PutMapping("/update/{id}")
    public Plant update(@PathVariable int id, @RequestBody Plant plant) {
        plant.setId(id);
        return plantService.updatePlant(plant);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        plantService.deletePlant(id);
    }
}
