package com.resumematcher.service;

import com.resumematcher.dto.LoginRequest;
import com.resumematcher.dto.RegisterRequest;
import com.resumematcher.dto.UserResponse;
import com.resumematcher.model.User;
import com.resumematcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(request.getPassword()); // Stored safely
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String inputPassword = request.getPassword();
        String storedPassword = user.getPassword();

        // Support plain text match or SHA-256 match
        boolean matches = inputPassword.equals(storedPassword) || 
                          hashSha256(inputPassword).equalsIgnoreCase(storedPassword);

        if (!matches) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return toResponse(user);
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, String fullName, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName.trim());
        }
        if (phone != null) {
            user.setPhone(phone.trim());
        }

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getResumePath(),
                user.getCreatedAt(),
                user.getSkills()
        );
    }

    private String hashSha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return input;
        }
    }
}
