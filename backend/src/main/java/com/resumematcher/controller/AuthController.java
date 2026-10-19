package com.resumematcher.controller;

import com.resumematcher.dto.ApiResponse;
import com.resumematcher.dto.LoginRequest;
import com.resumematcher.dto.RegisterRequest;
import com.resumematcher.dto.UserResponse;
import com.resumematcher.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.ok("Registration successful", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to register: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    @GetMapping("/me/{userId}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable Long userId) {
        try {
            UserResponse response = authService.getUserById(userId);
            return ResponseEntity.ok(ApiResponse.ok("Profile retrieved", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        try {
            String fullName = body.get("fullName");
            String phone = body.get("phone");
            UserResponse response = authService.updateProfile(userId, fullName, phone);
            return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
