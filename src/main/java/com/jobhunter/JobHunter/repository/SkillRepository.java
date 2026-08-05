package com.jobhunter.JobHunter.repository;


import com.jobhunter.JobHunter.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    boolean existsByName(String name);
}

