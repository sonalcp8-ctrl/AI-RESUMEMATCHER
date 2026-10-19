package com.resumematcher.service;

import com.resumematcher.dto.ApplicationDTO;
import com.resumematcher.model.Application;
import com.resumematcher.model.Job;
import com.resumematcher.model.User;
import com.resumematcher.repository.ApplicationRepository;
import com.resumematcher.repository.JobRepository;
import com.resumematcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MatcherService matcherService;

    @Transactional
    public ApplicationDTO applyToJob(Long userId, Long jobId) {
        if (applicationRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new IllegalArgumentException("You have already applied to this job");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        MatcherService.MatchResult result = matcherService.calculateMatch(user.getSkills(), job.getSkills());

        Application application = new Application(userId, jobId, result.getScore());
        Application saved = applicationRepository.save(application);

        return new ApplicationDTO(
                saved.getId(),
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getJobType(),
                saved.getMatchScore(),
                saved.getStatus(),
                saved.getAppliedDate()
        );
    }

    public List<ApplicationDTO> getUserApplications(Long userId) {
        List<Application> apps = applicationRepository.findByUserIdOrderByAppliedDateDesc(userId);
        if (apps.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> jobIds = apps.stream().map(Application::getJobId).collect(Collectors.toList());
        Map<Long, Job> jobsMap = jobRepository.findAllById(jobIds).stream()
                .collect(Collectors.toMap(Job::getId, j -> j));

        List<ApplicationDTO> dtoList = new ArrayList<>();
        for (Application app : apps) {
            Job job = jobsMap.get(app.getJobId());
            String title = job != null ? job.getTitle() : "Unknown Job";
            String company = job != null ? job.getCompany() : "Unknown Company";
            String location = job != null ? job.getLocation() : "Unknown Location";
            String jobType = job != null ? job.getJobType() : "Full-Time";

            dtoList.add(new ApplicationDTO(
                    app.getId(),
                    app.getJobId(),
                    title,
                    company,
                    location,
                    jobType,
                    app.getMatchScore(),
                    app.getStatus(),
                    app.getAppliedDate()
            ));
        }

        return dtoList;
    }
}
