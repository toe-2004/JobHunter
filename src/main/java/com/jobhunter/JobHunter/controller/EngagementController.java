package com.jobhunter.JobHunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import com.jobhunter.JobHunter.enumeration.EngagementStatus;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
import com.jobhunter.JobHunter.service.FreelancerService;

@Controller
public class EngagementController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EngagementRepository engagementRepository;

    @GetMapping("/hire-freelancer/{id}")
    public String showHireForm(
            @PathVariable Long id,
            Model model) {

        Freelancer freelancer =
                freelancerService.getFreelancerById(id);

        Engagement engagement = new Engagement();

        model.addAttribute("freelancer", freelancer);
        model.addAttribute("engagement", engagement);
        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        return "engagement/hire-freelancer";
    }

    @PostMapping("/hire-freelancer/{id}")
    public String hireFreelancer(
            @PathVariable Long id,
            @ModelAttribute Engagement engagement,
            @RequestParam Long categoryId,
            Authentication auth) {

        Freelancer freelancer =
                freelancerService.getFreelancerById(id);

        Employer employer =
                employerRepository
                        .findByUserEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employer not found"
                                ));

        Category category =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                ));

        
        engagement.setFreelancer(freelancer);
        engagement.setEmployer(employer);
        engagement.setCategory(category);

       
        engagement.setStatus(
                EngagementStatus.PENDING
        );

        engagement.setId(null);

        engagementRepository.save(engagement);

        return "redirect:/employer-engagements";
    }

    @GetMapping("/my-engagements")
    public String myEngagements(
            Model model,
            Authentication auth) {

        Freelancer freelancer =
                freelancerRepository
                        .findByUserEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Freelancer not found"
                                ));

        List<Engagement> engagements =
                engagementRepository
                        .findByFreelancer(freelancer);

        model.addAttribute(
                "engagements",
                engagements
        );

        return "engagement/my-engagements";
    }

    @GetMapping("/employer-engagements")
    public String employerEngagements(
            Model model,
            Authentication auth) {

        Employer employer =
                employerRepository
                        .findByUserEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employer not found"
                                ));

        List<Engagement> engagements =
                engagementRepository
                        .findByEmployer(employer);

        model.addAttribute(
                "engagements",
                engagements
        );

        return "engagement/employer-engagements";
    }

    @PostMapping("/engagement/{id}/accept")
    @Transactional
    public String acceptEngagement(
            @PathVariable Long id,
            Authentication auth) {

        Freelancer freelancer =
                freelancerRepository
                        .findByUserEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Freelancer not found"
                                ));

        Engagement engagement =
                engagementRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Engagement not found"
                                ));


       
        if (!engagement.getFreelancer()
                .getId()
                .equals(freelancer.getId())) {

            throw new RuntimeException(
                    "You are not allowed to accept this offer"
            );
        }

        if (engagement.getStatus()
                != EngagementStatus.PENDING) {

            throw new RuntimeException(
                    "This offer is no longer pending"
            );
        }

        engagement.setStatus(
                EngagementStatus.ACCEPTED
        );


        return "redirect:/my-engagements";
    }

    @PostMapping("/engagement/{id}/reject")
    @Transactional
    public String rejectEngagement(
            @PathVariable Long id,
            Authentication auth) {

        Freelancer freelancer =
                freelancerRepository
                        .findByUserEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Freelancer not found"
                                ));

        Engagement engagement =
                engagementRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Engagement not found"
                                ));

        if (!engagement.getFreelancer()
                .getId()
                .equals(freelancer.getId())) {

            throw new RuntimeException(
                    "You are not allowed to reject this offer"
            );
        }

        if (engagement.getStatus()
                != EngagementStatus.PENDING) {

            throw new RuntimeException(
                    "This offer is no longer pending"
            );
        }

        engagement.setStatus(
                EngagementStatus.REJECTED
        );


        return "redirect:/my-engagements";
    }
}