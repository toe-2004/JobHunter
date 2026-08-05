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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public List<Engagement> getEngagements() {
		return engagements;
	}

	public void setEngagements(List<Engagement> engagements) {
		this.engagements = engagements;
	}

	public List<FreelancerSkill> getFreelancerSkills() {
		return freelancerSkills;
	}

	public void setFreelancerSkills(List<FreelancerSkill> freelancerSkills) {
		this.freelancerSkills = freelancerSkills;
	}

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
