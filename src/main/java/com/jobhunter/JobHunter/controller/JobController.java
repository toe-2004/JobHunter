package com.jobhunter.JobHunter.controller;

import com.jobhunter.JobHunter.enumeration.DurationType;

import com.jobhunter.JobHunter.enumeration.EmploymentType;
import com.jobhunter.JobHunter.enumeration.ExperienceLevel;
import com.jobhunter.JobHunter.enumeration.JobStatus;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
import com.jobhunter.JobHunter.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class JobController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FreelancerRepository freelancerRepository;
    

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSkillRepository jobSkillRepository;
    
    @Autowired
    private JobService jobService;


    @GetMapping("/create-job")
    public String showCreateJobForm(Model model) {
        
        Authentication auth =
        SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Employer employer = employerRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        model.addAttribute("employer", employer);


        model.addAttribute("job", new Job());

        model.addAttribute(
                "skills",
                skillRepository.findAll()
        );

        model.addAttribute(
                "selectedSkillIds",
                new ArrayList<Long>()
        );

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        model.addAttribute(
                "employmentTypes",
                EmploymentType.values()
        );

        model.addAttribute(
                "durations",
                DurationType.values()
        );

        model.addAttribute(
                "experienceLevels",
                ExperienceLevel.values()
        );

        return "job/create-job";
    }


    @PostMapping("/create-job")
    public String createJob(
            Job job,
            @RequestParam(value = "skillIds", required = false)
            List<Long> skillIds) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        Employer employer = employerRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        job.setEmployer(employer);
        job.setStatus(JobStatus.OPEN);
        Job savedJob = jobRepository.save(job);
        if (skillIds != null) {

            for (Long skillId : skillIds) {

                Skill skill = skillRepository
                        .findById(skillId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill not found"
                                )
                        );

                JobSkill jobSkill = new JobSkill();

                jobSkill.setJob(savedJob);
                jobSkill.setSkill(skill);

                jobSkillRepository.save(jobSkill);
            }
        }
        return "redirect:/my-jobs";
    }
    

    
        @PostMapping("/job/skill")
        @ResponseBody
        public ResponseEntity<Skill> saveSkill(@RequestParam("name") String name) {

        String skillName = name == null ? "" : name.trim();

        if (skillName.isEmpty()) {
                return ResponseEntity.badRequest().build();
        }

        Optional<Skill> existingSkill =
                skillRepository.findByNameIgnoreCase(skillName);

        if (existingSkill.isPresent()) {
                return ResponseEntity.ok(existingSkill.get());
        }

        Skill newSkill = new Skill();
        newSkill.setName(skillName);

        Skill savedSkill = skillRepository.save(newSkill);

        return ResponseEntity.ok(savedSkill);
        }

        @GetMapping("/my-jobs")
        public String viewMyJobs(Model model) {

                Authentication auth =
                        SecurityContextHolder.getContext()
                                .getAuthentication();

                User user = userRepository
                        .findByEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

                Employer employer = employerRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Employer not found"));

                List<Job> jobs =
                        jobRepository.findByEmployer(employer);
                model.addAttribute("employer", employer);
                model.addAttribute("jobs", jobs);

                return "job/my-jobs";
        }



    
    @Transactional
    @PostMapping("/edit-job/{id}")
    public String updateJob(
            @PathVariable Long id,
            @ModelAttribute("job") Job job,
            @RequestParam(value = "skillIds", required = false)
            List<Long> skillIds) {

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        existingJob.setTitle(job.getTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setSalaryMin(job.getSalaryMin());
        existingJob.setSalaryMax(job.getSalaryMax());
        existingJob.setBudget(job.getBudget());
        existingJob.setDeadline(job.getDeadline());
        existingJob.setCategory(job.getCategory());
        existingJob.setEmploymentType(job.getEmploymentType());
        existingJob.setDuration(job.getDuration());
        existingJob.setExperienceLevel(job.getExperienceLevel());

        jobSkillRepository.deleteByJobId(id);
        jobSkillRepository.flush();

        if (skillIds != null) {

            for (Long skillId : skillIds) {

                Skill skill = skillRepository.findById(skillId)
                        .orElseThrow(() ->
                                new RuntimeException("Skill not found"));

                JobSkill jobSkill = new JobSkill();

                jobSkill.setJob(existingJob);
                jobSkill.setSkill(skill);

                jobSkillRepository.save(jobSkill);
            }
        }

        jobRepository.save(existingJob);

        return "redirect:/my-jobs";
    }
    
    @GetMapping("/edit-job/{id}")
    public String editJob(
            @PathVariable Long id,
            Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Employer employer = employerRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        List<Long> selectedSkillIds = job.getJobSkills()
                .stream()
                .map(jobSkill -> jobSkill.getSkill().getId())
                .toList();

        model.addAttribute("employer", employer);

        model.addAttribute("job", job);

        model.addAttribute(
                "selectedSkillIds",
                selectedSkillIds
        );

        model.addAttribute(
                "skills",
                skillRepository.findAll()
        );

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        model.addAttribute(
                "employmentTypes",
                EmploymentType.values()
        );

        model.addAttribute(
                "durations",
                DurationType.values()
        );

        model.addAttribute(
                "experienceLevels",
                ExperienceLevel.values()
        );

        return "job/create-job";
    }
    
    @PostMapping("/delete-job/{id}")
    @Transactional
    public String deleteJob(@PathVariable Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        jobSkillRepository.deleteByJobId(id);
        jobRepository.delete(job);

        return "redirect:/my-jobs";
    }


    @PostMapping("/job-status/{id}")
    @Transactional
    public String changeJobStatus(@PathVariable Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (job.getStatus() == JobStatus.OPEN) {
            job.setStatus(JobStatus.CLOSED);
        } else {
            job.setStatus(JobStatus.OPEN);
        }

        jobRepository.save(job);

        return "redirect:/my-jobs";
    }
    
    @GetMapping("/job-details/{id}")
    public String viewAvailableJobs(
            @PathVariable Long id,
            Model model,
            Authentication auth) {

        Job job = jobService.getJobById(id);

        model.addAttribute("job", job);

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FREELANCER"))) {

            Freelancer freelancer = freelancerRepository
                    .findByUserEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Freelancer not found"));

            boolean alreadyApplied =
                    applicationRepository.existsByFreelancerAndJob(freelancer, job);

            model.addAttribute("alreadyApplied", alreadyApplied);
        }
        model.addAttribute("currentPage", "jobs");

        return "job/job-details";
    }
    
    @GetMapping("/jobHome")
    public String jobs(Model model) {

        List<Job> jobs = jobService.getAllJobs();

        model.addAttribute("jobs", jobs);

        return "job/jobHome";
    }
    @GetMapping("/all-jobs")
    public String allJobs(Model model) {

        List<Job> jobs = jobRepository.findAll();

        model.addAttribute("jobs", jobs);
        model.addAttribute("currentPage", "jobs");
        return "job/all-jobs";
    }
   
    
   
}