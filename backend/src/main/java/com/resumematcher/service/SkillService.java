package com.resumematcher.service;

import com.resumematcher.model.Skill;
import com.resumematcher.model.User;
import com.resumematcher.repository.SkillRepository;
import com.resumematcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAllByOrderByCategoryAscSkillNameAsc();
    }

    public Set<Skill> getUserSkills(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return user.getSkills();
    }

    @Transactional
    public Set<Skill> addSkillToUser(Long userId, Long skillId, String skillName, String category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Skill skillToAdd;
        if (skillId != null) {
            skillToAdd = skillRepository.findById(skillId)
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + skillId));
        } else if (skillName != null && !skillName.trim().isEmpty()) {
            String trimmed = skillName.trim();
            skillToAdd = skillRepository.findBySkillNameIgnoreCase(trimmed)
                    .orElseGet(() -> skillRepository.save(new Skill(trimmed, category != null ? category.trim() : "Custom")));
        } else {
            throw new IllegalArgumentException("Either skillId or skillName must be provided");
        }

        user.getSkills().add(skillToAdd);
        userRepository.save(user);
        return user.getSkills();
    }

    @Transactional
    public Set<Skill> removeSkillFromUser(Long userId, Long skillId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.getSkills().removeIf(skill -> skill.getId().equals(skillId));
        userRepository.save(user);
        return user.getSkills();
    }

    @Transactional
    public Skill createSkill(String skillName, String category) {
        String trimmed = skillName.trim();
        return skillRepository.findBySkillNameIgnoreCase(trimmed)
                .orElseGet(() -> skillRepository.save(new Skill(trimmed, category != null ? category.trim() : "General")));
    }
}
