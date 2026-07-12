package com.jobhunter.JobHunter.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Freelancer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "phone")
	private String phone;

	@Column(name = "location")
	private String location;

	@Column(name = "summary", columnDefinition = "TEXT")
	private String summary;

	@Column(name = "experience", columnDefinition = "TEXT")
	private String experience;

	@Column(name = "education", columnDefinition = "TEXT")
	private String education;

	@OneToMany(mappedBy = "freelancer")
	private List<Application> applications;

	@OneToMany(mappedBy = "freelancer")
	private List<Engagement> engagements;

	@OneToMany(mappedBy = "freelancer")
	private List<FreelancerSkill> freelancerSkills;
}
