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

	@Column(name = "title")
	private String title;

	@Column(name = "phone")
	private String phone;

	@Column(name = "location")
	private String location;

	@Column(name = "summary")
	private String summary;

	@Column(name = "experience")
	private String experience;

	@Column(name = "education")
	private String education;

	@OneToMany(mappedBy = "freelancer")
	private List<Application> applications;

	@OneToMany(mappedBy = "freelancer")
	private List<Engagement> engagements;

	@OneToMany(mappedBy = "freelancer")
	private List<FreelancerSkill> freelancerSkills;
}
