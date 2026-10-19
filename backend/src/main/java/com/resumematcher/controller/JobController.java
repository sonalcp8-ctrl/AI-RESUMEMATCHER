package com.resumematcher.controller;

import com.resumematcher.dto.ApiResponse;
import com.resumematcher.dto.JobDTO;
import com.resumematcher.model.Job;
import com.resumematcher.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<ApiResponse> getJobs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Integer minScore
    ) {
        try {
            List<JobDTO> jobs = jobService.getJobsForUser(userId, search, location, jobType, minScore);
            return ResponseEntity.ok(ApiResponse.ok("Jobs retrieved successfully", jobs));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to fetch jobs: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getJobById(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId
    ) {
        try {
            JobDTO job = jobService.getJobById(id, userId);
            return ResponseEntity.ok(ApiResponse.ok("Job details retrieved", job));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to fetch job: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createJob(@RequestBody Job job, @RequestParam(required = false) List<Long> skillIds) {
        try {
            Job created = jobService.createJob(job, skillIds);
            return ResponseEntity.ok(ApiResponse.ok("Job created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create job: " + e.getMessage()));
        }
    }
}
