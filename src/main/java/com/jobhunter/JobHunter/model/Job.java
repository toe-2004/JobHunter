package com.jobhunter.JobHunter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jobhunter.JobHunter.enumeration.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
    
    @Column(name = "deadline")
    private LocalDateTime deadline;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

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
