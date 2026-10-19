package com.resumematcher.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "match_score")
    private Integer matchScore = 0;

    @Column(name = "status", length = 50)
    private String status = "Applied";

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;

    @PrePersist
    protected void onCreate() {
        if (this.appliedDate == null) {
            this.appliedDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "Applied";
        }
    }

    public Application() {}

    public Application(Long userId, Long jobId, Integer matchScore) {
        this.userId = userId;
        this.jobId = jobId;
        this.matchScore = matchScore;
        this.status = "Applied";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}
