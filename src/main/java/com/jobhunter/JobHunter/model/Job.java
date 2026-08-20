package com.jobhunter.JobHunter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jobhunter.JobHunter.enumeration.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "salary_min")
    private BigDecimal salaryMin;
    
    @Column(name = "salary_max")
    private BigDecimal salaryMax;
    
    @Column(name = "budget")
    private BigDecimal budget;
    
    @Column(name = "deadline")
    private LocalDateTime deadline;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

	@ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DurationType duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    @OneToMany(mappedBy = "job")
    private List<JobSkill> jobSkills;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    
    
    

    public String getTimeAgo() {

        if (createdAt == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();

        long days = Duration.between(createdAt, now).toDays();

        if (days == 0) {

            long hours = Duration.between(createdAt, now).toHours();

            if (hours == 0) {
                return "Just now";
            }

            if (hours == 1) {
                return "1 hour ago";
            }

            return hours + " hours ago";
        }

        if (days == 1) {
            return "1 day ago";
        }

        return days + " days ago";
    }
}
