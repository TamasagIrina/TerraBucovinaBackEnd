package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.image.ImageRequestDTO;
import com.example.collaborationtest.dto.image.ImageResponseDTO;
import com.example.collaborationtest.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products/images")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/get/ByProductId/{productId}")
    public ResponseEntity<List<ImageResponseDTO>> getAllImgByProducId(@PathVariable int productId) {
        return ResponseEntity.ok(imageService.findAllByProduct_Id(productId));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ImageResponseDTO>> getAll() {
        return ResponseEntity.ok(imageService.findAll());
    }

    @PostMapping("/auth/upload")
    public ResponseEntity<ImageResponseDTO> upload(
            @RequestParam int productId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String altText,
            @RequestParam(defaultValue = "0") Integer sortOrder,
            @RequestParam(defaultValue = "false") Boolean isPrimary
    ) throws IOException {
        ImageRequestDTO request = new ImageRequestDTO(productId, altText, sortOrder, isPrimary);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(request, file));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/set-primary/{id}")
    public ResponseEntity<ImageResponseDTO> setPrimary(@PathVariable int id) {
        return ResponseEntity.ok(imageService.setPrimary(id));
    }

    @PutMapping("/admin/reorder")
    public ResponseEntity<Void> reorder(@RequestBody List<Integer> orderedImageIds) {
        imageService.reorder(orderedImageIds);
        return ResponseEntity.noContent().build();
    }
}
