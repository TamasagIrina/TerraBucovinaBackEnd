package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Image;
import com.example.collaborationtest.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/products/images")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/getByProductId")
    public List<Image> getByProductId(@RequestParam int productId) {
        return imageService.findAllByProduct_Id(productId);
    }
}
