package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Image;
import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.service.ImageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products/images")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/getByProductId/{id}")
    public List<Image> getByProductId(@RequestParam int productId) {
        return imageService.findAllByProduct_Id(productId);
    }

    @PostMapping("/add")
    public Image addPlant(@RequestBody Image image) {
        image.setId(0);
        return imageService.add(image);
    }
}
