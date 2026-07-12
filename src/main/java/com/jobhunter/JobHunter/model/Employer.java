package com.jobhunter.JobHunter.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "company_name")
	private String companyName;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@Column(unique = true, nullable = false)
	private String companyEmail;

	@NotBlank
	@Column(name = "company_phone")
	private String companyPhone;

	@NotBlank
	@Column(name = "company_description", columnDefinition = "TEXT")
	private String companyDescription;

	@NotBlank
	@Column(name = "company_location")
	private String companyLocation;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@OneToMany(mappedBy = "employer")
	private List<Job> jobs;

	@OneToMany(mappedBy = "employer")
	private List<Engagement> engagements;
}
