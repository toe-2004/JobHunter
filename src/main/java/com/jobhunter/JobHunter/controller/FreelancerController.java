// 
package com.jobhunter.JobHunter.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.Application;
import com.jobhunter.JobHunter.model.Engagement;
import com.jobhunter.JobHunter.model.Freelancer;
import com.jobhunter.JobHunter.model.FreelancerSkill;
import com.jobhunter.JobHunter.model.Skill;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.ApplicationRepository;
import com.jobhunter.JobHunter.repository.EngagementRepository;
import com.jobhunter.JobHunter.repository.FreelancerRepository;
import com.jobhunter.JobHunter.repository.FreelancerSkillRepository;
import com.jobhunter.JobHunter.repository.SkillRepository;
import com.jobhunter.JobHunter.repository.UserRepository;
import com.jobhunter.JobHunter.service.FreelancerService;


@Controller
public class FreelancerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EngagementRepository engagementRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private FreelancerSkillRepository freelancerSkillRepository;


    // =========================================================
    // HELPER METHOD
    // Get currently logged-in user
    // =========================================================

    private User getCurrentUser() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }


    // =========================================================
    // FREELANCER DASHBOARD
    // =========================================================

        @GetMapping("/freelancer/dashboard")
        public String profile(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        Optional<Freelancer> optionalFreelancer =
                freelancerRepository.findByUser(user);

        if (optionalFreelancer.isEmpty()) {
                throw new RuntimeException("Freelancer not found");
        }

        Freelancer freelancer = optionalFreelancer.get();

        model.addAttribute("freelancer", freelancer);

        Long freelancerId = freelancer.getId();

        long appliedJobsCount =
                applicationRepository.countByFreelancerId(freelancerId);

        long hireRequestsCount =
                engagementRepository.countByFreelancerId(freelancerId);

        List<Application> recentApplications =
                applicationRepository
                        .findTop4ByFreelancerIdOrderByCreatedAtDesc(freelancerId);

        List<Engagement> recentHireRequests =
                engagementRepository
                        .findTop4ByFreelancerIdOrderByStartDateDesc(freelancerId);

        model.addAttribute("appliedJobsCount", appliedJobsCount);
        model.addAttribute("hireRequestsCount", hireRequestsCount);
        model.addAttribute("recentApplications", recentApplications);
        model.addAttribute("recentHireRequests", recentHireRequests);

        model.addAttribute("currentPage", "dashboard");

        return "freelancer/dashboard";
        }



    @GetMapping("/freelancer/create")
    public String createFreelancer(Model model) {

        User user = getCurrentUser();

        model.addAttribute("user", user);

        Optional<Freelancer> existingFreelancer =
                freelancerRepository.findByUser(user);

        if (existingFreelancer.isPresent()) {

            return "redirect:/freelancer/profile";
        }


        Freelancer freelancer =
                new Freelancer();


        model.addAttribute(
                "freelancer",
                freelancer
        );

        model.addAttribute(
                "skills",
                skillRepository.findAll()
        );

        model.addAttribute(
                "selectedSkillIds",
                new HashSet<Long>()
        );

        model.addAttribute(
                "skill",
                new Skill()
        );


        return "freelancer/freelancer-create";
    }


    @GetMapping("/freelancer/profile")
    public String viewProfile(Model model) {

        User user = getCurrentUser();

        model.addAttribute(
                "user",
                user
        );


        Optional<Freelancer> optionalFreelancer =
                freelancerRepository
                        .findByUser(user);

        if (optionalFreelancer.isEmpty()) {

            return "redirect:/freelancer/create";
        }


        Freelancer freelancer =
                optionalFreelancer.get();


        model.addAttribute(
                "freelancer",
                freelancer
        );
        model.addAttribute("currentPage", "profile_f");

        return "freelancer/freelancer-profile";
    }


    @Transactional
    @PostMapping("/freelancer/profile")
    public String saveProfile(

            @ModelAttribute("freelancer")
            Freelancer freelancer,

            @RequestParam(
                    value = "skillIds",
                    required = false
            )
            Set<Long> skillIds,

            @RequestParam(
                    value = "profilePhoto",
                    required = false
            )
            MultipartFile profilePhoto

    ) throws IOException {


        User user = getCurrentUser();


        Optional<Freelancer> optionalFreelancer =
                freelancerRepository
                        .findByUser(user);


        Freelancer savedFreelancer;



        if (optionalFreelancer.isEmpty()) {

            freelancer.setUser(user);

            savedFreelancer =
                    freelancerRepository.save(
                            freelancer
                    );


            user.setRole(Role.FREELANCER);

            userRepository.save(user);
        }


        else {

            savedFreelancer =
                    optionalFreelancer.get();


            savedFreelancer.setTitle(
                    freelancer.getTitle()
            );

            savedFreelancer.setPhone(
                    freelancer.getPhone()
            );

            savedFreelancer.setLocation(
                    freelancer.getLocation()
            );

            savedFreelancer.setSummary(
                    freelancer.getSummary()
            );

            savedFreelancer.setExperience(
                    freelancer.getExperience()
            );

            savedFreelancer.setEducation(
                    freelancer.getEducation()
            );


            savedFreelancer =
                    freelancerRepository.save(
                            savedFreelancer
                    );
        }


        freelancerSkillRepository
                .deleteByFreelancer(
                        savedFreelancer
                );


        if (skillIds != null &&
                !skillIds.isEmpty()) {


            for (Long skillId : skillIds) {

                Skill skill =
                        skillRepository
                                .findById(skillId)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Skill not found: "
                                                        + skillId
                                        )
                                );


                FreelancerSkill freelancerSkill =
                        new FreelancerSkill();


                freelancerSkill.setFreelancer(
                        savedFreelancer
                );

                freelancerSkill.setSkill(
                        skill
                );


                freelancerSkillRepository.save(
                        freelancerSkill
                );
            }
        }

        if (profilePhoto != null &&
                !profilePhoto.isEmpty()) {


            String originalFileName =
                    profilePhoto.getOriginalFilename();


            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;


            Path uploadPath =
                    Paths.get("uploads");


            if (!Files.exists(uploadPath)) {

                Files.createDirectories(
                        uploadPath
                );
            }


            Path filePath =
                    uploadPath.resolve(
                            fileName
                    );


            Files.copy(
                    profilePhoto.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            user.setProfilePhoto(
                    fileName
            );


            userRepository.save(user);
        }


        return "redirect:/freelancer/profile";
    }

    @GetMapping("/freelancer/edit")
    public String editFreelancer(Model model) {

        User user = getCurrentUser();


        Freelancer freelancer =
                freelancerRepository
                        .findByUser(user)
                        .orElse(null);

        if (freelancer == null) {

            return "redirect:/freelancer/create";
        }


        Set<Long> selectedSkillIds =
                new HashSet<>();


        if (freelancer.getFreelancerSkills() != null) {

            for (
                    FreelancerSkill freelancerSkill :
                    freelancer.getFreelancerSkills()
            ) {

                if (freelancerSkill.getSkill() != null) {

                    selectedSkillIds.add(
                            freelancerSkill
                                    .getSkill()
                                    .getId()
                    );
                }
            }
        }


        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "freelancer",
                freelancer
        );

        model.addAttribute(
                "skills",
                skillRepository.findAll()
        );

        model.addAttribute(
                "selectedSkillIds",
                selectedSkillIds
        );

        model.addAttribute(
                "skill",
                new Skill()
        );
        model.addAttribute("currentPage", "profile_f");

        return "freelancer/freelancer-edit";
    }


    @GetMapping("/freelancer/skill")
    public String showSkillForm(Model model) {

        model.addAttribute(
                "skill",
                new Skill()
        );

        return "freelancer/skillform";
    }


    @PostMapping("/freelancer/skill")
    public String saveSkill(
            @ModelAttribute("skill") Skill skill) {


        String skillName =
                skill.getName() == null
                        ? ""
                        : skill.getName().trim();


        if (skillName.isEmpty()) {

            throw new RuntimeException(
                    "Skill name cannot be empty"
            );
        }


        Optional<Skill> existingSkill =
                skillRepository
                        .findByNameIgnoreCase(
                                skillName
                        );


        if (existingSkill.isEmpty()) {

            Skill newSkill =
                    new Skill();

            newSkill.setName(
                    skillName
            );

            skillRepository.save(
                    newSkill
            );
        }


        return "redirect:/freelancer/edit";
    }

    @GetMapping("/freelancers")
    public String freelancers(Model model) {

        List<Freelancer> freelancers =
                freelancerService
                        .getAllFreelancers();


        model.addAttribute(
                "freelancers",
                freelancers
        );

        model.addAttribute(
                "currentPage",
                "freelancers"
        );


        return "freelancer/freelancerHome";
    }


    @GetMapping("/freelancer-details/{id}")
    public String freelancerDetails(
            @PathVariable Long id,
            Model model) {


        Freelancer freelancer =
                freelancerService
                        .getFreelancerById(id);


        model.addAttribute(
                "freelancer",
                freelancer
        );

        model.addAttribute(
                "currentPage",
                "freelancers"
        );


        return "freelancer/freelancer-details";
    }


    @GetMapping("/freelancer-application-lists")
    public String applicationList(Model model) {

        User user = getCurrentUser();

        model.addAttribute(
                "user",
                user
        );


        Freelancer freelancer =
                freelancerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Freelancer not found"
                                )
                        );


        List<Application> applications =
                applicationRepository
                        .findByFreelancerIdOrderByCreatedAtDesc(
                                freelancer.getId()
                        );


        model.addAttribute(
                "applications",
                applications
        );


        return "freelancer/freelancer-application-lists";
    }


    @GetMapping("/freelancer-recent-hire-requests")
    public String allHireRequests(Model model) {

        User user = getCurrentUser();

        model.addAttribute(
                "user",
                user
        );


        Freelancer freelancer =
                freelancerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Freelancer not found"
                                )
                        );


        List<Engagement> hireRequests =
                engagementRepository
                        .findByFreelancerIdOrderByStartDateDesc(
                                freelancer.getId()
                        );


        model.addAttribute(
                "hireRequests",
                hireRequests
        );


        return "freelancer/recent-hire-requests";
    }


    // =========================================================
    // PUBLIC FREELANCER PROFILE
    // =========================================================
    // If this is intended for viewing another person's profile,
    // you can keep this separate from /freelancer/profile.

    @GetMapping("/freelancer-profile")
    public String freelancerProfile(
            Authentication auth,
            Model model) {


        User user =
                userRepository
                        .findByEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        Freelancer freelancer =
                freelancerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Freelancer not found"
                                )
                        );


        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "freelancer",
                freelancer
        );


        return "freelancer/profile";
    }

}