package com.jobhunter.JobHunter.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.repository.JobRepository;

@Service
public class JobService {

	 private final JobRepository jobRepository;

	    public JobService(JobRepository jobRepository) {
	        this.jobRepository = jobRepository;
	    }

	    public Job getJobById(Long id) {
	        return jobRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
	    }
	    
	    public List<Job> getAllJobs() {
	        return jobRepository.findAll();
	    }
	    
	    public List<Job> getLatest4Jobs() {
	        return jobRepository.findTop4ByOrderByCreatedAtDesc();
	    }
	    
	    public List<Job> getLatest6Jobs() {
	        return jobRepository.findTop6ByOrderByCreatedAtDesc();
	    }

}
