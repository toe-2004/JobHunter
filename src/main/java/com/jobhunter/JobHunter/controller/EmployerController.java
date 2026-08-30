package com.jobhunter.JobHunter.controller;

import com.jobhunter.JobHunter.dto.EmployerProfileDto;

import com.jobhunter.JobHunter.dto.EmployerRegistrationDto;
import com.jobhunter.JobHunter.enumeration.ApplicationStatus;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jobhunter.JobHunter.enumeration.Role;
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
            @Valid @ModelAttribute("employerForm") EmployerRegistrationDto form, BindingResult result,Model model) {

    	User user = userService.findByEmail(userDetails.getUsername());
    	model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "employer/register";
        }
       
        try {
        	 
        employerService.createEmployer(user.getId(), form);

        user.setRole(Role.EMPLOYER);
        userRepository.save(user);

        Authentication currentAuth =
                SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth =
                new UsernamePasswordAuthenticationToken(
                        currentAuth.getPrincipal(),
                        currentAuth.getCredentials(),
                        List.of(
                                new SimpleGrantedAuthority("ROLE_EMPLOYER")
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        
        return "redirect:/employer/dashboard";
        } catch (IllegalStateException ex)
        		{ 
        		model.addAttribute("error", ex.getMessage()); 
        		model.addAttribute("employerForm", form); 
        			return "employer/register"; 
        		}
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

            @RequestParam(
                    value = "profilePhotoFile",
                    required = false
            )
            MultipartFile profilePhoto,

            Authentication auth,

            RedirectAttributes redirectAttributes,

            Model model) throws IOException {

        Employer employer = employerRepository
                .findByUserEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        User user = employer.getUser();

        // Validation errors
        if (bindingResult.hasErrors()) {

            model.addAttribute("employer", employer);
            model.addAttribute("currentPage", "profile_e");

            return "employer/profile-edit";
        }

        // Check duplicate email
        boolean emailExists =
                employerRepository.existsByCompanyEmailAndIdNot(
                        profileDto.getCompanyEmail().trim(),
                        employer.getId()
                );

        if (emailExists) {

            bindingResult.rejectValue(
                    "companyEmail",
                    "duplicate",
                    "Email is already registered."
            );

            profileDto.setProfilePhoto(
                    user.getProfilePhoto()
            );

            model.addAttribute("employer", employer);
            model.addAttribute("currentPage", "profile_e");

            return "employer/profile-edit";
        }

        // Update user information
        user.setName(profileDto.getName());

        // Update employer information
        employer.setCompanyName(
                profileDto.getCompanyName()
        );

        employer.setCompanyEmail(
                profileDto.getCompanyEmail()
        );

        employer.setCompanyPhone(
                profileDto.getCompanyPhone()
        );

        employer.setCompanyLocation(
                profileDto.getCompanyLocation()
        );

        employer.setCompanyDescription(
                profileDto.getCompanyDescription()
        );

        // ==========================================
        // PROFILE PHOTO
        // ==========================================

        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            Path uploadPath = Paths.get("uploads");

            // Create uploads folder if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Remember old photo
            String oldPhoto = user.getProfilePhoto();

            // Create new filename
            String originalFileName =
                    profilePhoto.getOriginalFilename();

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;

            Path newPhotoPath =
                    uploadPath.resolve(fileName);

            // Save NEW photo
            Files.copy(
                    profilePhoto.getInputStream(),
                    newPhotoPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Update database value
            user.setProfilePhoto(fileName);

            // Delete OLD photo
            if (oldPhoto != null && !oldPhoto.isEmpty()) {

                Path oldPhotoPath =
                        uploadPath
                                .resolve(oldPhoto)
                                .normalize();

                Files.deleteIfExists(oldPhotoPath);
            }
        }

        // Save changes
        userRepository.save(user);

        employerRepository.save(employer);

        return "redirect:/employer/profile";
    }
}
