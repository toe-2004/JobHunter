package com.jobhunter.JobHunter.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.jobhunter.JobHunter.enumeration.ApplicationStatus;
import com.jobhunter.JobHunter.enumeration.JobStatus;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.FreelancerService;
import com.jobhunter.JobHunter.service.JobService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {
	
	 
	@Autowired
    private EmployerRepository employerRepository;
	@Autowired
    private FreelancerRepository freelancerRepository;
	@Autowired
    private ApplicationRepository applicationRepository;
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

        List<Job> latestJobs = jobService.getLatestOpenJobs();

        
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
        long activeJobs =
                jobRepository.countByStatus(JobStatus.OPEN);

        long companies =
                employerRepository.count();

        long jobSeekers =
                freelancerRepository.count();

        long successfulHires =
                applicationRepository.countByStatus(
                        ApplicationStatus.ACCEPTED);

        model.addAttribute("activeJobs", activeJobs);
        model.addAttribute("companies", companies);
        model.addAttribute("jobSeekers", jobSeekers);
        model.addAttribute("successfulHires", successfulHires);
        model.addAttribute("jobs", jobs);
        model.addAttribute("latest6Jobs", latest6Jobs);
        model.addAttribute("categories", latest8Categories);
        model.addAttribute("latestJobs", latestJobs);
        model.addAttribute("latestEmployers", latestEmployers);
        model.addAttribute("freelancers", freelancers);
        model.addAttribute("currentPage", "home");

        Job featuredJob = latestJobs.isEmpty() ? null : latestJobs.getFirst();
        model.addAttribute("featuredJob", featuredJob);
        model.addAttribute("jobSeekerCount",
                featuredJob == null ? 0 : applicationRepository.countByJobId(featuredJob.getId()));

        return "viewHomeJob";
    }
    
 
    

}
