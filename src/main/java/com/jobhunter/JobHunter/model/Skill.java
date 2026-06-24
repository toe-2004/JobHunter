package com.jobhunter.JobHunter.model;
import java.util.List;
import jakarta.persistence.*;
@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name")
    private String name;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<JobSkill> getJobSkills() {
		return jobSkills;
	}

	public void setJobSkills(List<JobSkill> jobSkills) {
		this.jobSkills = jobSkills;
	}

	public List<FreelancerSkill> getFreelancerSkills() {
		return freelancerSkills;
	}

	public void setFreelancerSkills(List<FreelancerSkill> freelancerSkills) {
		this.freelancerSkills = freelancerSkills;
	}

	@OneToMany(mappedBy = "skill")
    private List<JobSkill> jobSkills;

    @OneToMany(mappedBy = "skill")
    private List<FreelancerSkill> freelancerSkills;
}
