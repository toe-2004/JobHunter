package com.jobhunter.JobHunter.controller;

import java.util.ArrayList;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;


@Controller
public class SearchController {
	
	 @Autowired
	 private JobRepository jobRepository;
	 
	 @Autowired
	 private FreelancerRepository freelancerRepository;

	 @GetMapping("/search")
	 public String search(
	         @RequestParam(value = "keyword", required = false) String keyword,
	         Model model) {

	     List<Job> jobs = new ArrayList<>();
	     Set<Freelancer> freelancerSet = new LinkedHashSet<>();

	     if (keyword != null && !keyword.trim().isEmpty()) {

	         keyword = keyword.trim();

	         // JOB SEARCH
	         jobs.addAll(
	             jobRepository.findByTitleContainingIgnoreCase(keyword)
	         );

	         jobs.addAll(
	             jobRepository.findByEmployerCompanyNameContainingIgnoreCase(keyword)
	         );

	         jobs.addAll(
	             jobRepository.findByCategoryNameContainingIgnoreCase(keyword)
	         );

	         freelancerSet.addAll(
	             freelancerRepository
	                 .findByUser_NameContainingIgnoreCase(keyword)
	         );

	         freelancerSet.addAll(
	             freelancerRepository
	                 .findByTitleContainingIgnoreCase(keyword)
	         );
	     }

	     List<Freelancer> freelancers = new ArrayList<>(freelancerSet);

	     model.addAttribute("jobs", jobs);
	     model.addAttribute("freelancers", freelancers);
	     model.addAttribute("keyword", keyword);

	     return "search/search-results";
	 }
}
