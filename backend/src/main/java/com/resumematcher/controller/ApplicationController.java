package com.resumematcher.controller;

import com.resumematcher.dto.ApiResponse;
import com.resumematcher.dto.ApplicationDTO;
import com.resumematcher.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse> apply(@RequestBody Map<String, Object> body) {
        try {
            Long userId = Long.valueOf(body.get("userId").toString());
            Long jobId = Long.valueOf(body.get("jobId").toString());
            ApplicationDTO dto = applicationService.applyToJob(userId, jobId);
            return ResponseEntity.ok(ApiResponse.ok("Applied successfully!", dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to apply: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserApplications(@PathVariable Long userId) {
        try {
            List<ApplicationDTO> applications = applicationService.getUserApplications(userId);
            return ResponseEntity.ok(ApiResponse.ok("Applications retrieved", applications));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to retrieve applications: " + e.getMessage()));
        }
    }
}
