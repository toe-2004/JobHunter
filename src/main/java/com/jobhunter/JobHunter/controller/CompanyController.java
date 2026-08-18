package com.jobhunter.JobHunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.service.EmployerService;

@Controller
public class CompanyController {


    @Autowired
    private EmployerService employerService;
	   @GetMapping("/companies")
	    public String companies(Model model) {

	        List<Employer> employers =
	                employerService.getAllEmployers();

	        model.addAttribute("employers", employers);

	        return "company/company-list";
	    }
	   
	   @GetMapping("/company/{id}")
	   public String companyDetail(
	           @PathVariable Long id,
	           Model model) {

	       Employer employer =
	               employerService.getEmployerById(id);

	       model.addAttribute("employer", employer);

	       return "company/company-detail";
	   }
}
