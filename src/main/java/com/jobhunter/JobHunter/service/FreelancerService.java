package com.jobhunter.JobHunter.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.Freelancer;
import com.jobhunter.JobHunter.model.FreelancerSkill;
import com.jobhunter.JobHunter.model.Skill;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.EmployerRepository;
import com.jobhunter.JobHunter.repository.FreelancerRepository;
import com.jobhunter.JobHunter.repository.FreelancerSkillRepository;
import com.jobhunter.JobHunter.repository.SkillRepository;
import com.jobhunter.JobHunter.repository.UserRepository;

@Service
public class FreelancerService {
  
	private final FreelancerRepository freelancerRepository;
	
	public FreelancerService(FreelancerRepository freelancerRepository) {
	    this.freelancerRepository = freelancerRepository;
	}
	 
	public List<Freelancer> getAllFreelancers() {
	    return freelancerRepository.findAll();
	    
	}
	
	
	public List<Freelancer> getLatest4Freelancers() {
	    return freelancerRepository.findTop4ByOrderByIdDesc();
	}
	
	public Freelancer getFreelancerById(Long id) {
	    return freelancerRepository.findById(id)
	            .orElseThrow(() ->
	                new RuntimeException("Freelancer not found with id: " + id));
	}
}
