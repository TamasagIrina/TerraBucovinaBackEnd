package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.service.PlantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/getById")
    public Plant getById(int id) {
        return this.plantService.findPlantById(id);
    }

    @GetMapping("/getByProductId")
    public List<Plant> getByProductId(int productId) {
        return this.plantService.findPlantProductById(productId);
    }

    @PostMapping("/add")
    public Plant addPlant(@RequestBody Plant plant) {
        return this.plantService.addPlant(plant);
    }
}
