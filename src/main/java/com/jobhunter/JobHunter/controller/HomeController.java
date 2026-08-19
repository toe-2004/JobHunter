package com.jobhunter.JobHunter.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.SkillRepository;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.FreelancerService;
import com.jobhunter.JobHunter.service.JobService;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {

	 
    @Autowired
    private FreelancerService freelancerService;
    @Autowired
    private EmployerService employerService;
	@Autowired
    private JobService jobService;

    @GetMapping("/index")
    public String home() {
        return "login";
    }
    @GetMapping("/test")
    public String test() {
        return "employer/test";
    }
    @GetMapping("/test1")
    public String test1() {
        return "employer/test1";
    }
    
  
    
    // @GetMapping("/login")
    // public String login() {
    //     return "login";
    // }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @GetMapping("/viewHomeJob")
    public String home(Model model) {

        List<Job> latestJobs = jobService.getLatest4Jobs();

        model.addAttribute("latestJobs", latestJobs);
        List<Freelancer> freelancers =
                freelancerService.getLatest4Freelancers();
        List<Employer> latestEmployers =
                employerService.getLatest4Employers();

        model.addAttribute("latestEmployers", latestEmployers);


        model.addAttribute("freelancers", freelancers);


        return "viewHomeJob";
    }
    
 

}
