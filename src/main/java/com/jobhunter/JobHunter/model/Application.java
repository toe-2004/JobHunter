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

	@Column(name = "cover_letter", columnDefinition = "TEXT", nullable = false)
	private String coverLetter;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "job_id", nullable = false)
	private Job job;

	@ManyToOne
	@JoinColumn(name = "freelancer_id", nullable = false)
	private Freelancer freelancer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status = ApplicationStatus.PENDING;
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}

}
