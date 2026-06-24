package com.jobhunter.JobHunter.model;
import java.util.List;
import jakarta.persistence.*;
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "category_name")
    private String categoryName;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return categoryName;
	}

	public void setName(String categoryName) {
		this.categoryName = categoryName;
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

    @OneToMany(mappedBy = "category")
    private List<Job> jobs;

    @OneToMany(mappedBy = "category")
    private List<Engagement> engagements;
}
