package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.service.PlantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products/plants")
public class PlantController {

    private PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
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
    public Plant addPlant(@RequestBody Plant plant) {
        plant.setId(0);
        return this.plantService.addPlant(plant);
    }
}
