package com.resumematcher.service;

import com.resumematcher.model.Skill;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatcherService {

    public static class MatchResult {
        private final int score;
        private final List<String> matchedSkills;
        private final List<String> missingSkills;

        public MatchResult(int score, List<String> matchedSkills, List<String> missingSkills) {
            this.score = score;
            this.matchedSkills = matchedSkills;
            this.missingSkills = missingSkills;
        }

        public int getScore() { return score; }
        public List<String> getMatchedSkills() { return matchedSkills; }
        public List<String> getMissingSkills() { return missingSkills; }
    }

    public MatchResult calculateMatch(Set<Skill> userSkills, Set<Skill> jobSkills) {
        if (jobSkills == null || jobSkills.isEmpty()) {
            return new MatchResult(0, Collections.emptyList(), Collections.emptyList());
        }

        Set<String> normalizedUserSkills = (userSkills == null ? Collections.<Skill>emptySet() : userSkills)
                .stream()
                .filter(Objects::nonNull)
                .map(s -> s.getSkillName().trim().toLowerCase())
                .collect(Collectors.toSet());

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (Skill jobSkill : jobSkills) {
            if (jobSkill == null || jobSkill.getSkillName() == null) continue;
            String originalName = jobSkill.getSkillName().trim();
            String normalized = originalName.toLowerCase();

            if (normalizedUserSkills.contains(normalized)) {
                matched.add(originalName);
            } else {
                missing.add(originalName);
            }
        }

        int totalRequired = jobSkills.size();
        int matchedCount = matched.size();
        int score = (int) Math.round((100.0 * matchedCount) / totalRequired);

        return new MatchResult(score, matched, missing);
    }
}
