package com.example.collaborationtest.service;

import com.example.collaborationtest.model.Image;
import com.example.collaborationtest.repository.ImageRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ImageService {

    private ImageRepo imageRepo;
    public ImageService(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    public List<Image> findAllByProduct_Id(int productId) {
            if(!imageRepo.findAllByProduct_Id(productId).isEmpty()) {
                return imageRepo.findAllByProduct_Id(productId);
            }
            return null;
    }

    public Image findPrimaryByProduct_Id(int productId) {
        List<Image> images = findAllByProduct_Id(productId);

        if (images == null || images.isEmpty()) {
            return null;
        }

        for (Image image : images) {
            if (Boolean.TRUE.equals(image.getIsPrimary())) {
                return image;
            }
        }

        return null;
    }

    public List<Image> findAll() {
        return imageRepo.findAll();
    }

    public Image getImageById(int id) {
        return imageRepo.findById(id).get();
    }

    public Image add(Image image) {
            return imageRepo.save(image);

    }

    public void delete(int id) {
        Image image = imageRepo.findById(id).get();
        imageRepo.delete(image);
    }
}
