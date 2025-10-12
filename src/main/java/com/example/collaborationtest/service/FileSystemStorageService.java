package com.example.collaborationtest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileSystemStorageService {

    private final Path rootDir;

    public FileSystemStorageService(@Value("${app.images.dir}") String imagesDir) throws IOException {
        this.rootDir = Paths.get(imagesDir.trim()).toAbsolutePath().normalize();
        Files.createDirectories(this.rootDir);
    }

    public record Stored(String filename, String publicUrl, Path absolutePath) {}

    public Stored saveProductImage(int productId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Fișier gol");
        if (file.getSize() > 5 * 1024 * 1024) // 5MB exemplu
            throw new IllegalArgumentException("Imagine prea mare (max 5MB)");
        String ct = file.getContentType() != null ? file.getContentType() : "";
        if (!ct.startsWith("image/"))
            throw new IllegalArgumentException("Accept doar imagini");

        // extensie sigură
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "img";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : ".webp";
        if (!ext.matches("\\.(png|jpe?g|webp|avif)$")) ext = ".webp";

        // subfolder per produs: imagesDir/products/<productId>/
        Path productDir = rootDir.resolve("products").resolve(String.valueOf(productId));
        Files.createDirectories(productDir);

        String filename = UUID.randomUUID() + ext;
        Path target = productDir.resolve(filename);

        // scriere atomică
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // URL relativ care va fi servit prin StaticResourceConfig
        String publicUrl = "/images/products/" + productId + "/" + filename;

        return new Stored(filename, publicUrl, target);
    }

    public boolean deleteByPublicUrl(String publicUrl) throws IOException {
        // optional utilitar: ștergere după /images/products/...
        if (publicUrl == null || !publicUrl.startsWith("/images/")) return false;
        Path p = rootDir.resolve(publicUrl.replaceFirst("^/images/?", ""));
        if (!p.normalize().startsWith(rootDir)) return false; // protecție path traversal
        return Files.deleteIfExists(p);
    }
}
