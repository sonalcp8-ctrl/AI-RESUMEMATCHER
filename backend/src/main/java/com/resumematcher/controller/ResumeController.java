package com.resumematcher.controller;

import com.resumematcher.dto.ApiResponse;
import com.resumematcher.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<ApiResponse> uploadResume(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Please select a file to upload"));
        }

        try {
            String fileName = fileStorageService.storeResume(userId, file);
            return ResponseEntity.ok(ApiResponse.ok("Resume uploaded successfully!", Map.of("fileName", fileName)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Could not upload file: " + e.getMessage()));
        }
    }

    @GetMapping("/download/{userId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long userId) {
        try {
            Resource resource = fileStorageService.loadResumeAsResource(userId);
            String contentType = "application/octet-stream";
            if (resource.getFilename() != null && resource.getFilename().toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
