package com.jobhunter.JobHunter.model;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Entity
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @NotBlank
    @Column(name="company_name")
    private String companyName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false)
    private String companyEmail;
    
    @NotBlank
    @Column(name = "company_phone")
    private String companyPhone;
    
    @NotBlank
    @Column(name = "company_description")
    private String companyDescription;
    
    @NotBlank
    @Column(name = "company_location")
    private String companyLocation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<Engagement> getEngagements() {
		return engagements;
	}

	public void setEngagements(List<Engagement> engagements) {
		this.engagements = engagements;
	}
	
    @OneToMany(mappedBy = "employer")
    private List<Job> jobs;

    @OneToMany(mappedBy = "employer")
    private List<Engagement> engagements;
}
