package com.jobhunter.JobHunter.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.CategoryRepository;
import com.jobhunter.JobHunter.repository.JobRepository;
import com.jobhunter.JobHunter.repository.SkillRepository;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.FreelancerService;
import com.jobhunter.JobHunter.service.JobService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {

	 
    @Autowired
    private FreelancerService freelancerService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private EmployerService employerService;
	@Autowired
    private JobService jobService;

//    @GetMapping("/index")
//    public String home() {
//        return "login";
//    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/viewHomeJob";
    }
    
    @GetMapping("/test")
    public String test() {
        return "employer/test";
    }
    @GetMapping("/test1")
    public String test1() {
        return "employer/test1";
    }
    
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
  
    
    @GetMapping("/viewHomeJob")
    public String home(@RequestParam(required = false) Long categoryId,Model model) {

        List<Job> latestJobs = jobService.getLatest3Jobs();

        model.addAttribute("latestJobs", latestJobs);
        List<Freelancer> freelancers =
                freelancerService.getLatest4Freelancers();
        List<Employer> latestEmployers =
                employerService.getLatest4Employers();
       
        List<Category> categories = categoryRepository.findAll();
        List<Category> latest8Categories = categories.stream()
                .limit(8)
                .toList();
        List<Job> latest6Jobs = jobService.getLatest6Jobs();
        
        List<Job> jobs;
        if (categoryId != null) {
            jobs = jobRepository.findByCategoryId(categoryId);
        } else {
            jobs = jobRepository.findAll();
        }

        model.addAttribute("jobs", jobs);

        

        model.addAttribute("latest6Jobs", latest6Jobs);

        model.addAttribute("categories", latest8Categories);

        model.addAttribute("latestEmployers", latestEmployers);


        model.addAttribute("freelancers", freelancers);
        model.addAttribute("currentPage", "home");

        return "viewHomeJob";
    }
    
 
    

}
