package com.resumematcher.dto;

public class SkillActionRequest {
    private Long skillId;
    private String skillName;
    private String category;

    public SkillActionRequest() {}

    public SkillActionRequest(Long skillId, String skillName, String category) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
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
}
