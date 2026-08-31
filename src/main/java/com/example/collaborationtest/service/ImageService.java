package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.image.ImageRequestDTO;
import com.example.collaborationtest.dto.image.ImageResponseDTO;
import com.example.collaborationtest.mapper.ImageMapper;
import com.example.collaborationtest.model.Image;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.ImageRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    private ImageRepo imageRepo;
    private final ImageMapper imageMapper;
    private final R2StorageService storage;

    public ImageService(ImageRepo imageRepo, ImageMapper imageMapper, R2StorageService storage) {
        this.imageRepo = imageRepo;
        this.imageMapper = imageMapper;
        this.storage = storage;
    }

    public List<ImageResponseDTO> findAllByProduct_Id(int productId) {
        List<Image> images = imageRepo.findAllByProduct_Id(productId);
        if (images.isEmpty()) {
            return null;
        }
        return imageMapper.toResponseList(images);
    }

    /**
     * Internal helper returning the primary image <em>entity</em> for a product.
     * Used by {@link EmailService}; not exposed through any controller.
     */
    public Image findPrimaryByProduct_Id(int productId) {
        List<Image> images = imageRepo.findAllByProduct_Id(productId);
        if (images.isEmpty()) {
            return null;
        }
        for (Image image : images) {
            if (Boolean.TRUE.equals(image.getIsPrimary())) {
                return image;
            }
        }
        return null;
    }

    public List<ImageResponseDTO> findAll() {
        return imageMapper.toResponseList(imageRepo.findAll());
    }

    /**
     * Stores an uploaded image for a product and persists its metadata.
     */
    public ImageResponseDTO upload(ImageRequestDTO request, MultipartFile file) throws IOException {
        var stored = storage.saveProductImage(request.productId(), file);

        Image img = Image.builder()
                .product(Product.builder().id(request.productId()).build())
                .imageUrl(stored.publicUrl())
                .altText(request.altText())
                .sortOrder(request.sortOrder() != null ? request.sortOrder() : 0)
                .isPrimary(request.isPrimary() != null ? request.isPrimary() : Boolean.FALSE)
                .build();

        return imageMapper.toResponse(imageRepo.save(img));
    }

    public void delete(int id) {
        Image image = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found: " + id));
        storage.deleteByPublicUrl(image.getImageUrl());
        imageRepo.delete(image);
    }
}
