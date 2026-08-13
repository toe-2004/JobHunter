package com.jobhunter.JobHunter.repository;


import com.jobhunter.JobHunter.model.Skill;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    boolean existsByName(String name);
    
    List<Skill> findAll();

    Optional<Skill> findByNameIgnoreCase(String name);
    
    
}



