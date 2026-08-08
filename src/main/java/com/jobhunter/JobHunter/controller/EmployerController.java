package com.jobhunter.JobHunter.controller;

import com.jobhunter.JobHunter.dto.EmployerProfileDto;
import com.jobhunter.JobHunter.dto.EmployerRegistrationDto;
import com.jobhunter.JobHunter.model.Application;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private EmployerService employerService;
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String page(Model model) {
        model.addAttribute("employerForm", new EmployerRegistrationDto());
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

        model.addAttribute("employer", employer);
        model.addAttribute("jobCount", employerService.getJobCount());
        model.addAttribute("recentJobs", recentJobs);
        model.addAttribute("recentApplications", recentApplications);
        return "employer/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Employer employer = employerService.getProfile();
        model.addAttribute("employer", employer);
        return "employer/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Employer employer = employerService.getProfile();
        EmployerProfileDto dto = EmployerProfileDto.from(employer.getUser(), employer);
        model.addAttribute("profile", dto);
        return "employer/profile-edit";
    }

    @PostMapping("/profile/edit")
    public String submitProfileEdit(@Valid @ModelAttribute("profile") EmployerProfileDto profileDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "employer/profile-edit";
        }

        try {
            employerService.updateProfile(profileDto);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
            return "redirect:/employer/profile";
        } catch (IllegalStateException ex) {
            bindingResult.reject("profile.error", ex.getMessage());
            return "employer/profile-edit";
        }
    }
}
