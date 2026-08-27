package com.jobhunter.JobHunter.controller;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.jobhunter.JobHunter.dto.ApplicationDto;
import com.jobhunter.JobHunter.enumeration.*;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

@Controller
public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FreelancerRepository freelancerRepository;

    private final Path uploadDirectory =Paths.get("uploads/cv");

    public ApplicationController(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository,
            FreelancerRepository freelancerRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.freelancerRepository = freelancerRepository;
    }

    //Show all jobs for freelancer
    @GetMapping("/jobs")
    public String viewAvailableJobs(Model model, Authentication auth) {

        List<Job> jobs = jobRepository.findAll();
        Freelancer freelancer = freelancerRepository
                .findByUserEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        Set<Long> appliedJobIds = new HashSet<>();

        for (Job job : jobs) {
            if (applicationRepository.existsByFreelancerAndJob(freelancer, job)) {
                appliedJobIds.add(job.getId());
            }
        }
        model.addAttribute("jobs", jobs);
        model.addAttribute("appliedJobIds", appliedJobIds);
        return "job/jobs";
    }

  
    //Show application form for freelancer
    @GetMapping("/apply-job/{id}")
    public String showApplicationForm(@PathVariable Long id,Model model) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new RuntimeException(
                    "This job is closed");
        }

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        if (!user.getRole().name().equals("FREELANCER")) {
            throw new RuntimeException("Only freelancers can apply for jobs");
        }

        Freelancer freelancer = freelancerRepository.findByUserEmail(auth.getName()).orElseThrow(() ->new RuntimeException("Freelancer not found"));
        if (applicationRepository.existsByJobAndFreelancer(job,freelancer)) {
        		throw new RuntimeException("You have already applied for this job");
        }

        model.addAttribute("job", job);
        model.addAttribute("applicationDto",new ApplicationDto());
        return "application/apply";
    }

    //Create application
    @PostMapping("/apply-job/{id}")
    public String applyJob(@PathVariable Long id,@ModelAttribute ApplicationDto dto) {
        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->new RuntimeException("User not found"));

        if (!user.getRole().name().equals("FREELANCER")) {
            throw new RuntimeException("Only freelancers can apply for jobs");
        }

        Freelancer freelancer =freelancerRepository.findByUserEmail(auth.getName()).orElseThrow(() ->new RuntimeException("Freelancer not found"));

        Job job = jobRepository.findById(id).orElseThrow(() ->new RuntimeException("Job not found"));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new RuntimeException("This job is closed");
        }

        if (applicationRepository.existsByJobAndFreelancer( job,freelancer)) {
            throw new RuntimeException("You have already applied for this job");
        }

        MultipartFile cv = dto.getCurriculumVitae();
        if (cv == null || cv.isEmpty()) {
            throw new RuntimeException("Please upload your CV");
        }

        String originalFileName = cv.getOriginalFilename();

        if (originalFileName == null) {
            throw new RuntimeException("Invalid CV file");
        }

        String lowerFileName = originalFileName.toLowerCase();
        if (!lowerFileName.endsWith(".pdf")
                && !lowerFileName.endsWith(".doc")
                && !lowerFileName.endsWith(".docx")) {

            throw new RuntimeException("Only PDF, DOC and DOCX files are allowed");
        }
        try {
            Files.createDirectories(uploadDirectory);

        } catch (IOException e) {

            throw new RuntimeException("Could not create upload directory",e);
        }

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf(".");

        if (dotIndex >= 0) {
        	extension = originalFileName.substring(dotIndex);
        }

        String newFileName = UUID.randomUUID().toString()+ extension;
        Path filePath = uploadDirectory.resolve(newFileName);
        try {

            Files.copy(cv.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save CV file",e);
        }

        Application application = new Application();
        application.setJob(job);
        application.setFreelancer(freelancer);

        application.setCurriculumVitae(filePath.toString());
        application.setCoverLetter(dto.getCoverLetter());
        application.setStatus(ApplicationStatus.PENDING);
        applicationRepository.save(application);
        return "redirect:/my-applications";
    }


    //View application for freelancer
    @GetMapping("/my-applications")
    public String myApplications(Model model) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Freelancer freelancer = freelancerRepository
                .findByUserEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        List<Application> applications =
                applicationRepository.findByFreelancerOrderByCreatedAtDesc(freelancer);

        model.addAttribute("user", user);
        model.addAttribute("freelancer", freelancer);
        model.addAttribute("applications", applications);
        model.addAttribute("currentPage", "my-applications");
        return "application/my-applications";
    }

    //Withdraw freelancer's application
    @PostMapping("/application/{id}/withdraw")
    public String withdrawApplication(@PathVariable Long id) {

        Application application =  applicationRepository.findById(id).orElseThrow(() ->new RuntimeException("Application not found"));
        if (application.getStatus() != ApplicationStatus.PENDING
                && application.getStatus() != ApplicationStatus.SHORTLISTED) {

            throw new RuntimeException("This application cannot be withdrawn");
        }
        applicationRepository.delete(application);

        return "redirect:/my-applications";
    }

    //View freelancer's application for employer
    @GetMapping("/employer/jobs/{jobId}/applications")
    public String viewApplications(@PathVariable Long jobId,Model model) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        List<Application> applications = applicationRepository.findByJob(job);
        Employer employer = job.getEmployer();
        model.addAttribute("employer", employer);
        model.addAttribute("job",job);
        model.addAttribute("applications",applications);
        model.addAttribute("currentPage","my-jobs");
        return "application/employer-applications";
    }

    //Employer change application status
    @PostMapping("/employer/applications/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id,@RequestParam ApplicationStatus status) {

        Application application = applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        ApplicationStatus currentStatus = application.getStatus();
        if (currentStatus == ApplicationStatus.PENDING) {
            if (status != ApplicationStatus.SHORTLISTED
                    &&
                status != ApplicationStatus.REJECTED) {
                throw new RuntimeException("Invalid status change");
            }
        }

        else if (currentStatus == ApplicationStatus.SHORTLISTED) {
            if (status!= ApplicationStatus.ACCEPTED
                    &&
                status!= ApplicationStatus.REJECTED) {

                throw new RuntimeException("Invalid status change");
            }
        }
        else {

            throw new RuntimeException("Application status cannot be changed");
        }

        application.setStatus(status);
        applicationRepository.save(application);


        return "redirect:/employer/jobs/"+ application.getJob().getId()+ "/applications";
    }
    
    //Download CV
    @GetMapping("/application/{id}/cv")
    public ResponseEntity<Resource> downloadCV(@PathVariable Long id) {

        Application application = applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));
        String filePath = application.getCurriculumVitae();

        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException("CV not found");
        }

        try {
            Path path = Paths.get(filePath);
            Resource resource = new FileSystemResource(path);

            if (!resource.exists()) {
                throw new RuntimeException("CV file does not exist");
            }

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                            path.getFileName().toString() +
                            "\""
                    )
                    .contentType(
                            MediaType.APPLICATION_PDF
                    )
                    .body(resource);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not download CV", e);
        }
    }
}