package com.resumematcher.controller;

import com.resumematcher.dto.ApiResponse;
import com.resumematcher.dto.SkillActionRequest;
import com.resumematcher.model.Skill;
import com.resumematcher.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllSkills() {
        try {
            List<Skill> skills = skillService.getAllSkills();
            return ResponseEntity.ok(ApiResponse.ok("Skills retrieved successfully", skills));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to retrieve skills: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserSkills(@PathVariable Long userId) {
        try {
            Set<Skill> skills = skillService.getUserSkills(userId);
            return ResponseEntity.ok(ApiResponse.ok("User skills retrieved successfully", skills));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to retrieve user skills: " + e.getMessage()));
        }
    }

    @PostMapping("/user/{userId}/add")
    public ResponseEntity<ApiResponse> addSkillToUser(@PathVariable Long userId, @RequestBody SkillActionRequest request) {
        try {
            Set<Skill> updatedSkills = skillService.addSkillToUser(
                    userId, 
                    request.getSkillId(), 
                    request.getSkillName(), 
                    request.getCategory()
            );
            return ResponseEntity.ok(ApiResponse.ok("Skill added successfully", updatedSkills));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to add skill: " + e.getMessage()));
        }
    }

    @DeleteMapping("/user/{userId}/remove/{skillId}")
    public ResponseEntity<ApiResponse> removeSkillFromUser(@PathVariable Long userId, @PathVariable Long skillId) {
        try {
            Set<Skill> updatedSkills = skillService.removeSkillFromUser(userId, skillId);
            return ResponseEntity.ok(ApiResponse.ok("Skill removed successfully", updatedSkills));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to remove skill: " + e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createSkill(@RequestBody SkillActionRequest request) {
        try {
            Skill skill = skillService.createSkill(request.getSkillName(), request.getCategory());
            return ResponseEntity.ok(ApiResponse.ok("Skill created successfully", skill));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create skill: " + e.getMessage()));
        }
    }
}
