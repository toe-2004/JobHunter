package com.jobhunter.JobHunter.model;

import java.time.LocalDateTime;
import com.jobhunter.JobHunter.enumeration.ApplicationStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "curriculum_vitae")
	private String curriculumVitae;

	@Column(name = "cover_letter", columnDefinition = "TEXT")
	private String coverLetter;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;

	@ManyToOne
	@JoinColumn(name = "freelancer_id")
	private Freelancer freelancer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status = ApplicationStatus.PENDING;
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}

}
