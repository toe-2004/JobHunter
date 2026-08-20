package com.jobhunter.JobHunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.JobService;

@Controller
public class CompanyController {


    @Autowired
    private EmployerService employerService;
    @Autowired
    private JobService jobService;
	   @GetMapping("/companies")
	    public String companies(Model model) {

	        List<Employer> employers =
	                employerService.getAllEmployers();

	        model.addAttribute("employers", employers);
			model.addAttribute("currentPage","companies");
	        return "company/company-list";
	    }
	   
	   @GetMapping("/company/{id}")
	   public String companyDetail(
	           @PathVariable Long id,
	           Model model) {

	       Employer employer = employerService.getEmployerById(id);

	       model.addAttribute("employer", employer);

	       List<Job> jobs = jobService.getJobsByEmployer(employer);

	       model.addAttribute("jobs", jobs);

	       return "company/company-detail";
	   }
}
