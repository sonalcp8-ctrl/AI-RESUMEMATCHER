package com.resumematcher.service;

import com.resumematcher.dto.JobDTO;
import com.resumematcher.model.Application;
import com.resumematcher.model.Job;
import com.resumematcher.model.Skill;
import com.resumematcher.model.User;
import com.resumematcher.repository.ApplicationRepository;
import com.resumematcher.repository.JobRepository;
import com.resumematcher.repository.SkillRepository;
import com.resumematcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private MatcherService matcherService;

    public List<JobDTO> getJobsForUser(Long userId, String search, String location, String jobType, Integer minScore) {
        List<Job> allJobs = jobRepository.findAll();

        Set<Skill> userSkills = Collections.emptySet();
        Map<Long, Application> userApplications = new HashMap<>();

        if (userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                userSkills = userOpt.get().getSkills();
                List<Application> apps = applicationRepository.findByUserIdOrderByAppliedDateDesc(userId);
                for (Application app : apps) {
                    userApplications.put(app.getJobId(), app);
                }
            }
        }

        final Set<Skill> finalUserSkills = userSkills;
        List<JobDTO> dtoList = new ArrayList<>();

        for (Job job : allJobs) {
            MatcherService.MatchResult result = matcherService.calculateMatch(finalUserSkills, job.getSkills());

            JobDTO dto = new JobDTO();
            dto.setId(job.getId());
            dto.setTitle(job.getTitle());
            dto.setCompany(job.getCompany());
            dto.setLocation(job.getLocation());
            dto.setJobType(job.getJobType());
            dto.setSalaryRange(job.getSalaryRange());
            dto.setDescription(job.getDescription());
            dto.setExperienceReq(job.getExperienceReq());
            dto.setPostedDate(job.getPostedDate());
            dto.setSkills(job.getSkills());
            dto.setMatchScore(result.getScore());
            dto.setMatchedSkills(result.getMatchedSkills());
            dto.setMissingSkills(result.getMissingSkills());

            Application app = userApplications.get(job.getId());
            if (app != null) {
                dto.setHasApplied(true);
                dto.setApplicationStatus(app.getStatus());
            } else {
                dto.setHasApplied(false);
                dto.setApplicationStatus(null);
            }

            dtoList.add(dto);
        }

        // Filtering
        return dtoList.stream()
                .filter(j -> {
                    if (search != null && !search.trim().isEmpty()) {
                        String s = search.trim().toLowerCase();
                        boolean matchTitle = j.getTitle() != null && j.getTitle().toLowerCase().contains(s);
                        boolean matchCompany = j.getCompany() != null && j.getCompany().toLowerCase().contains(s);
                        boolean matchDesc = j.getDescription() != null && j.getDescription().toLowerCase().contains(s);
                        boolean matchSkill = j.getSkills() != null && j.getSkills().stream().anyMatch(sk -> sk.getSkillName().toLowerCase().contains(s));
                        if (!matchTitle && !matchCompany && !matchDesc && !matchSkill) return false;
                    }
                    if (location != null && !location.trim().isEmpty() && !location.equalsIgnoreCase("All")) {
                        if (j.getLocation() == null || !j.getLocation().toLowerCase().contains(location.trim().toLowerCase())) {
                            return false;
                        }
                    }
                    if (jobType != null && !jobType.trim().isEmpty() && !jobType.equalsIgnoreCase("All")) {
                        if (j.getJobType() == null || !j.getJobType().equalsIgnoreCase(jobType.trim())) {
                            return false;
                        }
                    }
                    if (minScore != null && minScore > 0) {
                        if (j.getMatchScore() < minScore) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());
    }

    public JobDTO getJobById(Long jobId, Long userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        Set<Skill> userSkills = Collections.emptySet();
        Application userApp = null;

        if (userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                userSkills = userOpt.get().getSkills();
                userApp = applicationRepository.findByUserIdAndJobId(userId, jobId).orElse(null);
            }
        }

        MatcherService.MatchResult result = matcherService.calculateMatch(userSkills, job.getSkills());

        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setCompany(job.getCompany());
        dto.setLocation(job.getLocation());
        dto.setJobType(job.getJobType());
        dto.setSalaryRange(job.getSalaryRange());
        dto.setDescription(job.getDescription());
        dto.setExperienceReq(job.getExperienceReq());
        dto.setPostedDate(job.getPostedDate());
        dto.setSkills(job.getSkills());
        dto.setMatchScore(result.getScore());
        dto.setMatchedSkills(result.getMatchedSkills());
        dto.setMissingSkills(result.getMissingSkills());

        if (userApp != null) {
            dto.setHasApplied(true);
            dto.setApplicationStatus(userApp.getStatus());
        } else {
            dto.setHasApplied(false);
            dto.setApplicationStatus(null);
        }

        return dto;
    }

    @Transactional
    public Job createJob(Job job, List<Long> skillIds) {
        if (skillIds != null && !skillIds.isEmpty()) {
            Set<Skill> skills = new HashSet<>(skillRepository.findAllById(skillIds));
            job.setSkills(skills);
        }
        return jobRepository.save(job);
    }
}
