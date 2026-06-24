package com.jobhunter.JobHunter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.jobhunter.JobHunter.enumeration.EngagementStatus;

import jakarta.persistence.*;
@Entity
public class Engagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name="description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "monthly_rate")
    private BigDecimal monthlyRate;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    public BigDecimal getMonthlyRate() {
		return monthlyRate;
	}
	public void setMonthlyRate(BigDecimal monthlyRate) {
		this.monthlyRate = monthlyRate;
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
	
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public Employer getEmployer() {
		return employer;
	}
	public void setEmployer(Employer employer) {
		this.employer = employer;
	}
	public Freelancer getFreelancer() {
		return freelancer;
	}
	public void setFreelancer(Freelancer freelancer) {
		this.freelancer = freelancer;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public EngagementStatus getStatus() {
		return status;
	}
	public void setStatus(EngagementStatus status) {
		this.status = status;
	}
	
	@ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private Freelancer freelancer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private EngagementStatus status;
}
