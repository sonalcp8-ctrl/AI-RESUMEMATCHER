package com.resumematcher.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name", nullable = false, unique = true, length = 100)
    private String skillName;

    @Column(name = "category", length = 50)
    private String category;

    public Skill() {}

    public Skill(String skillName, String category) {
        this.skillName = skillName;
        this.category = category;
    }

    public Skill(Long id, String skillName, String category) {
        this.id = id;
        this.skillName = skillName;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id) || 
               (skillName != null && skillName.equalsIgnoreCase(skill.skillName));
    }

    @Override
    public int hashCode() {
        return skillName != null ? skillName.toLowerCase().hashCode() : 0;
    }
}
