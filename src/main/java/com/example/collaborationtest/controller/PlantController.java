package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.plant.PlantRequestDTO;
import com.example.collaborationtest.dto.plant.PlantResponseDTO;
import com.example.collaborationtest.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products/plants")
public class PlantController {

    private PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PlantResponseDTO>> getAll() {
        return ResponseEntity.ok(plantService.findAllPlants());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PlantResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(plantService.findPlantById(id));
    }

    @GetMapping("/getByProductId/{productId}")
    public ResponseEntity<List<PlantResponseDTO>> getByProductId(@PathVariable int productId) {
        return ResponseEntity.ok(plantService.findPlantProductById(productId));
    }

    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlantResponseDTO> addPlant(
            @Valid @RequestPart("plant") PlantRequestDTO plant,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantService.addPlant(plant, file));
    }

    @PutMapping(value = "/admin/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlantResponseDTO> updatePlant(
            @PathVariable int id,
            @Valid @RequestPart("plant") PlantRequestDTO plant,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(plantService.updatePlant(id, plant, file));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }
}
