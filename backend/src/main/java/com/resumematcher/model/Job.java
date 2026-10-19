package com.resumematcher.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "company", nullable = false, length = 100)
    private String company;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "job_type", length = 50)
    private String jobType = "Full-Time";

    @Column(name = "salary_range", length = 50)
    private String salaryRange;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "experience_req", length = 50)
    private String experienceReq;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "job_skills",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (this.postedDate == null) {
            this.postedDate = LocalDate.now();
        }
    }

    public Job() {}

    public Job(String title, String company, String location, String jobType, String salaryRange, String description, String experienceReq) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.jobType = jobType;
        this.salaryRange = salaryRange;
        this.description = description;
        this.experienceReq = experienceReq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperienceReq() {
        return experienceReq;
    }

    public void setExperienceReq(String experienceReq) {
        this.experienceReq = experienceReq;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
}
