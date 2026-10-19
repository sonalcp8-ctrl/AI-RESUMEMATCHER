package com.resumematcher.service;

import com.resumematcher.model.User;
import com.resumematcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private UserRepository userRepository;

    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory.", ex);
        }
    }

    public String storeResume(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        String rawFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (rawFilename.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + rawFilename);
        }

        String extension = "";
        int i = rawFilename.lastIndexOf('.');
        if (i > 0) {
            extension = rawFilename.substring(i);
        }

        String targetFileName = "resume_user_" + userId + "_" + System.currentTimeMillis() + extension;
        Path targetLocation = this.fileStorageLocation.resolve(targetFileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            user.setResumePath(targetFileName);
            userRepository.save(user);
            return targetFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + targetFileName, ex);
        }
    }

    public Resource loadResumeAsResource(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (user.getResumePath() == null || user.getResumePath().isEmpty()) {
            throw new IllegalArgumentException("No resume uploaded for user: " + userId);
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(user.getResumePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new IllegalArgumentException("Resume file not found: " + user.getResumePath());
            }
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Resume file not found: " + user.getResumePath(), ex);
        }
    }
}
