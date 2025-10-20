package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.repository.PlantRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {
    public PlantRepo plantRepo;



    public PlantService(PlantRepo plantRepo) {
        this.plantRepo = plantRepo;

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
        if(findPlantsByName(plant.getName()) != null) {
            return null;
        }
        return plantRepo.save(plant);
    }


    public Plant updatePlant(Plant plant) {
        Plant oldPlant = findPlantById(plant.getId());
        oldPlant.setName(plant.getName());
        oldPlant.setDescription(plant.getDescription());
        oldPlant.setBenefit(plant.getBenefit());
        return plantRepo.save(oldPlant);
    }

    public void deletePlant(int id) {
        Plant oldPlant = findPlantById(id);
        plantRepo.delete(oldPlant);
    }
}
