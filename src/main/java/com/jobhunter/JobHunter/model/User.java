package com.jobhunter.JobHunter.model;

import com.jobhunter.JobHunter.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name is required")
	@Column(unique = true, nullable = false)
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@Column(unique = true, nullable = false)
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Column(nullable = false)
	private String password;

	@Column(name = "profile_photo")
	private String profilePhoto;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@OneToOne(mappedBy = "user")
	private Employer employer;

	@OneToOne(mappedBy = "user")
	private Freelancer freelancer;
}
