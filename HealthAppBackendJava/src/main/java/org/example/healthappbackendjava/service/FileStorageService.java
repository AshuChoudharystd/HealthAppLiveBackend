package org.example.healthappbackendjava.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final Cloudinary cloudinary;
    private final boolean cloudinaryEnabled;
    private final Path uploadDirectory;
    private final String baseUrl;

    public FileStorageService(
            Cloudinary cloudinary,
            @Value("${cloudinary.enabled:false}") boolean cloudinaryEnabled,
            @Value("${file-storage.local-directory:uploads/profile-pictures}") String uploadDirectory,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.cloudinary = cloudinary;
        this.cloudinaryEnabled = cloudinaryEnabled;
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
        this.baseUrl = baseUrl.replaceAll("/+$", "");
    }

    public String uploadFile(MultipartFile file) {
        validateImage(file);

        if (cloudinaryEnabled) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.emptyMap()
                );
                return uploadResult.get("secure_url").toString();
            } catch (IOException ignored) {
                // Keep profile uploads available when the external provider is unavailable.
            }
        }

        return storeLocally(file);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select an image to upload");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Image must be smaller than 5 MB");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPEG, PNG, WebP, and GIF images are supported");
        }
    }

    private String storeLocally(MultipartFile file) {
        try {
            Files.createDirectories(uploadDirectory);
            String filename = UUID.randomUUID() + extensionFor(file.getContentType());
            Path destination = uploadDirectory.resolve(filename).normalize();
            if (!destination.getParent().equals(uploadDirectory)) {
                throw new IllegalArgumentException("Invalid upload filename");
            }
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/profile-pictures/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Unable to store profile image", e);
        }
    }

    private String extensionFor(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> throw new IllegalArgumentException("Unsupported image type");
        };
    }
}
