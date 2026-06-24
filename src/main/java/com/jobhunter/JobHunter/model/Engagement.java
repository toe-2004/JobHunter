package com.jobhunter.JobHunter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.jobhunter.JobHunter.enumeration.EngagementStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Engagement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "monthly_rate")
	private BigDecimal monthlyRate;

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;

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
	@Column(nullable = false)
	private EngagementStatus status = EngagementStatus.PENDING;
}
