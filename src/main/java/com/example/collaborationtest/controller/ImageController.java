package com.example.collaborationtest.controller;

import com.example.collaborationtest.model.Image;
import com.example.collaborationtest.model.Plant;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.service.FileSystemStorageService;
import com.example.collaborationtest.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/products/images")
public class ImageController {

    private ImageService imageService;

    private final FileSystemStorageService storage;


    public ImageController(ImageService imageService, FileSystemStorageService storage) {
        this.imageService = imageService;
        this.storage = storage;
    }

    @GetMapping("/get/ByProductId/{productId}")
    public List<Image> getAllImgByProducId(@PathVariable int productId) {
        return imageService.findAllByProduct_Id(productId);
    }

    @PostMapping("/auth/upload")
    public Image upload(
            @RequestParam int productId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String altText,
            @RequestParam(defaultValue = "0") Integer sortOrder,
            @RequestParam(defaultValue = "false") Boolean isPrimary
    ) throws IOException {

        var stored = storage.saveProductImage(productId, file);



        Image img = Image.builder()
                .product(Product.builder().id(productId).build())
                .imageUrl(stored.publicUrl())      // ← URL relativ, nu localhost
                .altText(altText)
                .sortOrder(sortOrder)
                .isPrimary(isPrimary)
                .build();

        return imageService.add(img);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable int id) {
        imageService.delete(id);
    }
}
