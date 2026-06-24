package com.jobhunter.JobHunter.model;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

	@OneToMany(mappedBy = "skill")
    private List<JobSkill> jobSkills;

    @OneToMany(mappedBy = "skill")
    private List<FreelancerSkill> freelancerSkills;
}
