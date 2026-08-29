package com.jobhunter.JobHunter.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
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


    @GetMapping("/freelancer/dashboard")
       public String profile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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

        long appliedJobsCount = applicationRepository.countByFreelancerId(freelancerId);
        long hireRequestsCount = engagementRepository.countByFreelancerId(freelancerId);

        List<Application> recentApplications = applicationRepository.findTop4ByFreelancerIdOrderByCreatedAtDesc(freelancerId);
        List<Engagement> recentHireRequests = engagementRepository.findTop4ByFreelancerIdOrderByStartDateDesc(freelancerId);

        model.addAttribute("appliedJobsCount", appliedJobsCount);
        model.addAttribute("hireRequestsCount", hireRequestsCount);
        model.addAttribute("recentApplications", recentApplications);
        model.addAttribute("recentHireRequests", recentHireRequests);
        model.addAttribute("currentPage", "dashboard");

        return "freelancer/dashboard";
        }



    @GetMapping("/freelancer/create")
    public String createFreelancer(Model model) {

    	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

         User user = userRepository.findByEmail(auth.getName())
                 .orElseThrow(() -> new RuntimeException("User not found"));

         
         Authentication newAuth = new UsernamePasswordAuthenticationToken(
                         auth.getPrincipal(),
                         auth.getCredentials(),
                         List.of(
                                 new SimpleGrantedAuthority("ROLE_FREELANCER")
                         )
                 );

         SecurityContextHolder.getContext().setAuthentication(newAuth);
        model.addAttribute("user", user);

        Optional<Freelancer> existingFreelancer =
                freelancerRepository.findByUser(user);

        if (existingFreelancer.isPresent()) {

            return "redirect:/freelancer/profile";
        }


        Freelancer freelancer = new Freelancer();
        model.addAttribute("freelancer",freelancer);
        model.addAttribute("skills",skillRepository.findAll());
        model.addAttribute("selectedSkillIds",new HashSet<Long>());
        model.addAttribute("skill",new Skill());
        return "freelancer/freelancer-create";
    }


    @GetMapping("/freelancer/profile")
    public String viewProfile(Model model) {

    	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

         User user = userRepository.findByEmail(auth.getName())
                 .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user",user);

        Optional<Freelancer> optionalFreelancer = freelancerRepository.findByUser(user);

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
    	@PostMapping("/freelancer/create")
    public String createFreelancer(

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
        MultipartFile profilePhoto,

        Authentication auth

) throws IOException {

    User user = userRepository.findByEmail(auth.getName())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (freelancerRepository.findByUser(user).isPresent()) {
        return "redirect:/freelancer/edit";
    }

    freelancer.setUser(user);

    Freelancer savedFreelancer = freelancerRepository.save(freelancer);

    user.setRole(Role.FREELANCER);
    if (profilePhoto != null && !profilePhoto.isEmpty()) {

        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName =
                profilePhoto.getOriginalFilename();

        String fileName =
                UUID.randomUUID()
                        + "_"
                        + originalFileName;

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                profilePhoto.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        user.setProfilePhoto(fileName);
    }

    userRepository.save(user);
    if (skillIds != null && !skillIds.isEmpty()) {

        for (Long skillId : skillIds) {

            Skill skill = skillRepository
                    .findById(skillId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Skill not found: " + skillId
                            )
                    );

            FreelancerSkill freelancerSkill =
                    new FreelancerSkill();

            freelancerSkill.setFreelancer(
                    savedFreelancer
            );

            freelancerSkill.setSkill(skill);

            freelancerSkillRepository.save(
                    freelancerSkill
            );
        }
    }

    return "redirect:/freelancer/profile";
    }
    
    @GetMapping("/freelancer/edit")
    public String editFreelancer(Model model) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Freelancer freelancer =
                freelancerRepository
                        .findByUser(user)
                        .orElse(null);

        if (freelancer == null) {
            return "redirect:/freelancer/create";
        }

        Set<Long> selectedSkillIds = new HashSet<>();

        if (freelancer.getFreelancerSkills() != null) {

            for (FreelancerSkill freelancerSkill :
                    freelancer.getFreelancerSkills()) {

                if (freelancerSkill.getSkill() != null) {

                    selectedSkillIds.add(
                            freelancerSkill
                                    .getSkill()
                                    .getId()
                    );
                }
            }
        }

        model.addAttribute("user", user);

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

        model.addAttribute(
                "currentPage",
                "profile_f"
        );

        return "freelancer/freelancer-edit";
    }
    
    @Transactional
    @PostMapping("/freelancer/edit")
    public String updateFreelancer(
        @ModelAttribute("freelancer")
        Freelancer freelancer,
        @RequestParam(
                value = "name",
                required = false
        )
        String name,
        @RequestParam(
                value = "skillIds",
                required = false
        )
        Set<Long> skillIds,
        @RequestParam(
                value = "profilePhoto",
                required = false
        )
        MultipartFile profilePhoto,Authentication auth) throws IOException {


    		User user = userRepository
            .findByEmail(auth.getName())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));


    Freelancer existingFreelancer =
            freelancerRepository
                    .findByUser(user)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Freelancer profile not found"
                            ));


    if (name != null && !name.trim().isEmpty()) {

        user.setName(name.trim());

    }


    existingFreelancer.setTitle(
            freelancer.getTitle()
    );

    existingFreelancer.setPhone(
            freelancer.getPhone()
    );

    existingFreelancer.setLocation(
            freelancer.getLocation()
    );

    existingFreelancer.setSummary(
            freelancer.getSummary()
    );

    existingFreelancer.setExperience(
            freelancer.getExperience()
    );

    existingFreelancer.setEducation(
            freelancer.getEducation()
    );


    freelancerRepository.save(
            existingFreelancer
    );


    freelancerSkillRepository
            .deleteByFreelancer(
                    existingFreelancer
            );


    if (skillIds != null && !skillIds.isEmpty()) {

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
                    existingFreelancer
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


        Path uploadPath =
                Paths.get("uploads");

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(
                    uploadPath
            );

        }


        if (user.getProfilePhoto() != null &&
                !user.getProfilePhoto().isEmpty()) {


            Path oldPhotoPath =
                    uploadPath
                            .resolve(
                                    user.getProfilePhoto()
                            )
                            .normalize();


            Files.deleteIfExists(
                    oldPhotoPath
            );
        }


        String originalFileName =
                profilePhoto.getOriginalFilename();


        String fileName =
                UUID.randomUUID()
                        + "_"
                        + originalFileName;


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
    }
    userRepository.save(user);

    return "redirect:/freelancer/profile";
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

    	 Authentication auth =
                 SecurityContextHolder.getContext().getAuthentication();

         User user = userRepository.findByEmail(auth.getName())
                 .orElseThrow(() -> new RuntimeException("User not found"));

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

    	 Authentication auth =
                 SecurityContextHolder.getContext().getAuthentication();

         User user = userRepository.findByEmail(auth.getName())
                 .orElseThrow(() -> new RuntimeException("User not found"));

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