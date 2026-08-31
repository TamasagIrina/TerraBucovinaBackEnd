package com.example.collaborationtest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * Stores product/plant images in Cloudflare R2 (S3-compatible object
 * storage) instead of the local filesystem — local disk doesn't survive a
 * container redeploy, R2 does, and its free tier has zero egress fees.
 */
@Service
public class R2StorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String publicUrl;

    public R2StorageService(S3Client s3Client,
                             @Value("${app.r2.bucket-name}") String bucketName,
                             @Value("${app.r2.public-url}") String publicUrl) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.publicUrl = publicUrl.replaceAll("/+$", "");
    }

    public record Stored(String filename, String publicUrl, String key) {}

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

        String filename = UUID.randomUUID() + ext;
        String key = "products/" + productId + "/" + filename;

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(ct)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return new Stored(filename, publicUrl + "/" + key, key);
    }

    public boolean deleteByPublicUrl(String url) {
        if (url == null || !url.startsWith(publicUrl + "/")) return false;
        String key = url.substring(publicUrl.length() + 1);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        return true;
    }
}
