package com.resumematcher.dto;

import com.resumematcher.model.Skill;
import java.time.LocalDateTime;
import java.util.Set;

public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String resumePath;
    private LocalDateTime createdAt;
    private Set<Skill> skills;

    public UserResponse() {}

    public UserResponse(Long id, String fullName, String email, String phone, String resumePath, LocalDateTime createdAt, Set<Skill> skills) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.resumePath = resumePath;
        this.createdAt = createdAt;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
}
