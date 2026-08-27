package com.jobhunter.JobHunter.controller;

import com.jobhunter.JobHunter.dto.EmployerProfileDto;
import com.jobhunter.JobHunter.dto.EmployerRegistrationDto;
import com.jobhunter.JobHunter.enumeration.ApplicationStatus;
import com.jobhunter.JobHunter.model.Application;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.ApplicationRepository;
import com.jobhunter.JobHunter.repository.EmployerRepository;
import com.jobhunter.JobHunter.repository.UserRepository;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private EmployerService employerService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployerRepository employerRepository;

    @GetMapping("/register")
    public String page(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("employerForm", new EmployerRegistrationDto());

        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        return "employer/register";
    }

    @PostMapping("/register")
    public String registerEmployer(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("employerForm") EmployerRegistrationDto form, BindingResult result) {

        if (result.hasErrors()) {
            return "employer/register";
        }
        User user = userService.findByEmail(userDetails.getUsername());
        employerService.createEmployer(user.getId(), form);

        return "redirect:/employer/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Employer employer = employerService.getProfile();
        List<Job> recentJobs = employerService.getRecentJobs();
        List<Application> recentApplications = employerService.getRecentApplications();

        long pendingCount = applicationRepository.countByJobEmployerAndStatus(employer,ApplicationStatus.PENDING);

        long shortlistedCount = applicationRepository.countByJobEmployerAndStatus(employer,ApplicationStatus.SHORTLISTED);

        model.addAttribute("pendingCount", pendingCount);

        model.addAttribute("shortlistedCount", shortlistedCount);
        model.addAttribute("employer", employer);
        model.addAttribute("jobCount", employerService.getJobCount());
        model.addAttribute("recentJobs", recentJobs);
        model.addAttribute("recentApplications", recentApplications);
        model.addAttribute("currentPage", "dashboard_e");
        return "employer/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Employer employer = employerService.getProfile();
        model.addAttribute("employer", employer);
        model.addAttribute("currentPage", "profile_e");
        return "employer/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Employer employer = employerService.getProfile();
        EmployerProfileDto dto = EmployerProfileDto.from(employer.getUser(), employer);
        model.addAttribute("profile", dto);
        model.addAttribute("employer",employer);
        model.addAttribute("currentPage", "profile_e");
        return "employer/profile-edit";
    }

    
    @PostMapping("/profile/edit")
    public String submitProfileEdit(
            @Valid @ModelAttribute("profile") EmployerProfileDto profileDto,
            BindingResult bindingResult,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "employer/profile-edit";
        }

        try {

            employerService.updateProfile(profileDto, profilePhoto);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile updated successfully."
            );

            return "redirect:/employer/profile";

        } catch (IllegalStateException ex) {

            bindingResult.reject(
                    "profile.error",
                    ex.getMessage()
            );

            return "employer/profile-edit";
        }
    }
    
    public long getPendingCount(Employer employer) {

        return applicationRepository.countByJobEmployerAndStatus(employer,ApplicationStatus.PENDING);
    }

    public long getShortlistedCount(Employer employer) {

        return applicationRepository.countByJobEmployerAndStatus(employer,ApplicationStatus.SHORTLISTED);
    }
 
    
    

    @PostMapping("/profile-update")
    public String updateProfile(
    		@Valid @ModelAttribute("profile") EmployerProfileDto profileDto,
    	    BindingResult bindingResult,
    	    @RequestParam(value = "profilePhoto", required = false)
    	    MultipartFile profilePhoto,
    	    Authentication auth,
    	    RedirectAttributes redirectAttributes) throws IOException {

        Employer employer = employerRepository.findByUserEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException("Employer not found"));

        User user = employer.getUser();

        user.setName(profileDto.getName());
        employer.setCompanyName(profileDto.getCompanyName());
        employer.setCompanyEmail(profileDto.getCompanyEmail());
        employer.setCompanyPhone(profileDto.getCompanyPhone());
        employer.setCompanyLocation(profileDto.getCompanyLocation());
        employer.setCompanyDescription(profileDto.getCompanyDescription());
        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            String uploadDir = "uploads/";

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + profilePhoto.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);

            Files.write(filePath,profilePhoto.getBytes());

            user.setProfilePhoto(fileName);
        }

        userRepository.save(user);

        return "redirect:/employer/profile";
    }
}
