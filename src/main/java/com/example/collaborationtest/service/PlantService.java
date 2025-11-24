package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.PlantRepo;
import com.example.collaborationtest.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {
    public PlantRepo plantRepo;

    public ProductRepo productRepo;



    public PlantService(PlantRepo plantRepo, ProductRepo productRepo) {
        this.plantRepo = plantRepo;
        this.productRepo = productRepo;

    }

    public Plant findPlantById(int id) {

        if(plantRepo.findById(id).isPresent()) {
            return plantRepo.findById(id).get();
        }
        return null;
    }

    public List<Plant> findAllPlants() {
        return plantRepo.findAll();
    }

    public Plant findPlantsByName(String name) {

        if (findPlantsByName(name)  == null) {
            return null;
        }

        return findPlantsByName(name);
    }

    public List<Plant> findPlantProductById(int id) {

        if(plantRepo.findAllByProduct_Id(id).isEmpty()) {
            return null;
        }
        return plantRepo.findAllByProduct_Id(id);
    }

    public Plant addPlant(Plant plant) {

        if ( plant.getProduct().getId() == 0) {
            throw new RuntimeException("Product is missing!");
        }

        // Dacă ai nevoie, încarci produsul real din DB
        Product product = productRepo.findById(plant.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        plant.setProduct(product);

        return plantRepo.save(plant);
    }

    public Plant updatePlant(Plant plant) {

        // Găsim planta existentă
        Plant existing = plantRepo.findById(plant.getId())
                .orElseThrow(() -> new RuntimeException("Plant not found: " + plant.getId()));

        // Actualizăm doar câmpurile necesare
        existing.setName(plant.getName());
        existing.setShortDescription(plant.getShortDescription());
        existing.setLongDescription(plant.getLongDescription());
        existing.setPlantMessage(plant.getPlantMessage());
        existing.setImageUrl(plant.getImageUrl());
        existing.setProduct(plant.getProduct()); // important!

        return plantRepo.save(existing);
    }


    public void deletePlant(int id) {
        Plant oldPlant = findPlantById(id);
        plantRepo.delete(oldPlant);
    }
}
