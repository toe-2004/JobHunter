package com.jobhunter.JobHunter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

import com.jobhunter.JobHunter.enumeration.*;

import jakarta.persistence.*;
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "salary_min")
    private BigDecimal salaryMin;
    
    @Column(name = "salary_max")
    private BigDecimal salaryMax;
    
    @Column(name = "budget")
    private BigDecimal budget;
    
    @Column(name = "dead_line")
    private LocalDateTime deadline;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(BigDecimal salaryMin) {
		this.salaryMin = salaryMin;
	}

	public BigDecimal getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(BigDecimal salaryMax) {
		this.salaryMax = salaryMax;
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public DurationType getDuration() {
		return duration;
	}

	public void setDuration(DurationType duration) {
		this.duration = duration;
	}

	public ExperienceLevel getExperienceLevel() {
		return experienceLevel;
	}

	public void setExperienceLevel(ExperienceLevel experienceLevel) {
		this.experienceLevel = experienceLevel;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public List<JobSkill> getJobSkills() {
		return jobSkills;
	}

	public void setJobSkills(List<JobSkill> jobSkills) {
		this.jobSkills = jobSkills;
	}

	@ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    
    @Enumerated(EnumType.STRING)
    private DurationType duration;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    @OneToMany(mappedBy = "job")
    private List<JobSkill> jobSkills;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
