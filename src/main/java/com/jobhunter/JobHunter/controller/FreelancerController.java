// package com.jobhunter.JobHunter.controller;
// import java.util.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;

// import com.jobhunter.JobHunter.enumeration.Role;
// import com.jobhunter.JobHunter.model.*;
// import com.jobhunter.JobHunter.repository.*;
// import com.jobhunter.JobHunter.service.UserService;

// @Controller
// public class FreelancerController {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private UserService userService;

//     @Autowired
//     private FreelancerRepository freelancerRepository;

//     @Autowired
//     private SkillRepository skillRepository;

//     @Autowired
//     private FreelancerSkillRepository freelancerSkillRepository;


//      @GetMapping("/freelancer/dashboard")
//     public String profile(Model model) {
//         User user = userService.getCurrentUser();
//         model.addAttribute("user", user);
//         Authentication auth =
//                 SecurityContextHolder.getContext().getAuthentication();

//         user = userRepository.findByEmail(auth.getName())
//                 .orElseThrow(() ->
//                         new RuntimeException("User not found"));

//         Optional<Freelancer> optionalFreelancer =
//                 freelancerRepository.findByUser(user);
//         if (optionalFreelancer.isPresent()) {
//             model.addAttribute("freelancer", optionalFreelancer.get());
//         }
//         return "freelancer/dashboard";
//     }


//      @GetMapping("/freelancer/create")
//     public String createFreelancer(Model model) {
//         Authentication auth =
//                 SecurityContextHolder.getContext().getAuthentication();

//         User user = userRepository.findByEmail(auth.getName())
//                 .orElseThrow(() ->
//                         new RuntimeException("User not found"));
//                         model.addAttribute("user", user);
//                          Freelancer freelancer = new Freelancer();
//         model.addAttribute("freelancer",freelancer);
//         model.addAttribute("skills", skillRepository.findAll());
//         model.addAttribute("selectedSkillIds", new HashSet<Long>());
//         return "freelancer/freelancer-create";
//     }

    
//     @GetMapping("/freelancer/profile")
//     public String viewProfile(Model model) {

//         Authentication auth =
//                 SecurityContextHolder.getContext().getAuthentication();

//         User user = userRepository.findByEmail(auth.getName())
//                 .orElseThrow(() ->
//                         new RuntimeException("User not found"));
//         model.addAttribute("user", user);
//         Optional<Freelancer> optionalFreelancer =
//                 freelancerRepository.findByUser(user);

//         if (optionalFreelancer.isPresent()) {

//             Freelancer freelancer = optionalFreelancer.get();

//             model.addAttribute(
//                     "freelancer",
//                     freelancer
//             );

//             return "freelancer/freelancer-profile";
//         }

//         Freelancer freelancer = new Freelancer();
//         model.addAttribute("freelancer",freelancer);
//         model.addAttribute("skills", skillRepository.findAll());
//         return "freelancer/freelancer-create";
//     }
    
    
//     @Transactional
//     @PostMapping("/freelancer/profile")
//     public String saveProfile(
//             @ModelAttribute("freelancer") Freelancer freelancer,
//             @RequestParam( value = "skillIds", required = false)Set<Long> skillIds) {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->new RuntimeException("User not found"));

//         Optional<Freelancer> optionalFreelancer = freelancerRepository.findByUser(user);
//         Freelancer savedFreelancer;
//         if (optionalFreelancer.isEmpty()) {
//             freelancer.setUser(user);
//             savedFreelancer = freelancerRepository.save(freelancer);

//             user.setRole(Role.FREELANCER);
//             userRepository.save(user);
//         }

//         else {
//             savedFreelancer =
//                     optionalFreelancer.get();


//             savedFreelancer.setTitle(
//                     freelancer.getTitle());

//             savedFreelancer.setPhone(
//                     freelancer.getPhone());

//             savedFreelancer.setLocation(
//                     freelancer.getLocation());

//             savedFreelancer.setSummary(
//                     freelancer.getSummary());

//             savedFreelancer.setExperience(
//                     freelancer.getExperience());

//             savedFreelancer.setEducation(
//                     freelancer.getEducation());


//             savedFreelancer =
//                     freelancerRepository.save(
//                             savedFreelancer);
//         }

//         freelancerSkillRepository
//                 .deleteByFreelancer(savedFreelancer);

//         if (skillIds != null) {

//             for (Long skillId : skillIds) {


//                 Skill skill =
//                         skillRepository.findById(skillId)
//                                 .orElseThrow(() ->
//                                         new RuntimeException(
//                                                 "Skill not found: "
//                                                         + skillId));

//                 FreelancerSkill freelancerSkill =
//                         new FreelancerSkill();


//                 freelancerSkill.setFreelancer(
//                         savedFreelancer);

//                 freelancerSkill.setSkill(
//                         skill);


//                 freelancerSkillRepository.save(
//                         freelancerSkill);
//             }
//         }


//         return "redirect:/freelancer/profile";
//     }

//     @GetMapping("/freelancer/skill")
//     public String showSkillForm(Model model) {
//         model.addAttribute("skill",new Skill());
//         return "freelancer/skillform";
//     }

//     @PostMapping("/freelancer/skill")
//     public String saveSkill(@ModelAttribute("skill") Skill skill) {

//         String skillName = skill.getName() == null
//                         ? ""
//                         : skill.getName().trim();

//         if (skillName.isEmpty()) {
//             throw new RuntimeException("Skill name cannot be empty");
//         }

//         Optional<Skill> existingSkill = skillRepository.findByNameIgnoreCase(skillName);
//         if (existingSkill.isEmpty()) {

//             Skill newSkill = new Skill();
//             newSkill.setName(skillName);
//             skillRepository.save(newSkill);
//         }

//         return "redirect:/freelancer/edit";
//     }
    
//     @GetMapping("/freelancer/edit")
//     public String editFreelancer(Model model) {

//         Authentication auth =
//                 SecurityContextHolder.getContext().getAuthentication();

//         User user = userRepository.findByEmail(auth.getName())
//                 .orElseThrow(() ->
//                         new RuntimeException("User not found"));

//         Optional<Freelancer> optionalFreelancer =
//                 freelancerRepository.findByUser(user);

//         if (optionalFreelancer.isEmpty()) {

//             model.addAttribute("freelancer", new Freelancer());
//             model.addAttribute("skills",skillRepository.findAll());
//             return "freelancer/freelancer-create";
//         }

//         Freelancer freelancer = optionalFreelancer.get();

//         Set<Long> selectedSkillIds = new HashSet<>();

//         for (FreelancerSkill freelancerSkill :
//                 freelancer.getFreelancerSkills()) {

//             selectedSkillIds.add(
//                     freelancerSkill.getSkill().getId()
//             );
//         }

//         model.addAttribute(
//                 "freelancer",
//                 freelancer
//         );

 
//         model.addAttribute(
//                 "skills",
//                 skillRepository.findAll()
//         );

//         model.addAttribute(
//                 "selectedSkillIds",
//                 selectedSkillIds
//         );

//         return "freelancer/freelancer-create";
//     }
// }
package com.jobhunter.JobHunter.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
import com.jobhunter.JobHunter.service.FreelancerService;
import com.jobhunter.JobHunter.service.UserService;


@Controller
public class FreelancerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private FreelancerSkillRepository freelancerSkillRepository;


    // =========================================================
    // FREELANCER DASHBOARD
    // =========================================================

    @GetMapping("/freelancer/dashboard")
    public String profile(Model model) {

        User user = userService.getCurrentUser();

        model.addAttribute("user", user);

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Optional<Freelancer> optionalFreelancer =
                freelancerRepository.findByUser(user);

        if (optionalFreelancer.isPresent()) {

            model.addAttribute(
                    "freelancer",
                    optionalFreelancer.get()
            );
        }

        return "freelancer/dashboard";
    }


    // =========================================================
    // CREATE FREELANCER PROFILE
    // =========================================================

    @GetMapping("/freelancer/create")
    public String createFreelancer(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        model.addAttribute("user", user);

        Freelancer freelancer = new Freelancer();

        model.addAttribute(
                "freelancer",
                freelancer
        );

        // All available skills
        model.addAttribute(
                "skills",
                skillRepository.findAll()
        );

        // Selected skills
        model.addAttribute(
                "selectedSkillIds",
                new HashSet<Long>()
        );

        // IMPORTANT:
        // This is used by the Create Skill popup
        model.addAttribute(
                "skill",
                new Skill()
        );

        return "freelancer/freelancer-create";
    }


    // =========================================================
    // VIEW FREELANCER PROFILE
    // =========================================================

    @GetMapping("/freelancer/profile")
    public String viewProfile(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        model.addAttribute("user", user);

        Optional<Freelancer> optionalFreelancer =
                freelancerRepository.findByUser(user);


        if (optionalFreelancer.isPresent()) {

            Freelancer freelancer =
                    optionalFreelancer.get();

            model.addAttribute(
                    "freelancer",
                    freelancer
            );

            return "freelancer/freelancer-profile";
        }


        // If profile doesn't exist yet

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


    // =========================================================
    // SAVE / UPDATE FREELANCER PROFILE
    // =========================================================

    @Transactional
    @PostMapping("/freelancer/profile")
    public String saveProfile(
            @ModelAttribute("freelancer") Freelancer freelancer,
            @RequestParam(
                    value = "skillIds",
                    required = false
            )
            Set<Long> skillIds) {


        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        Optional<Freelancer> optionalFreelancer =
                freelancerRepository.findByUser(user);


        Freelancer savedFreelancer;


        // =====================================================
        // CREATE NEW FREELANCER
        // =====================================================

        if (optionalFreelancer.isEmpty()) {

            freelancer.setUser(user);

            savedFreelancer =
                    freelancerRepository.save(freelancer);


            user.setRole(Role.FREELANCER);

            userRepository.save(user);
        }


        // =====================================================
        // UPDATE EXISTING FREELANCER
        // =====================================================

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


        // =====================================================
        // UPDATE SKILLS
        // =====================================================

        freelancerSkillRepository
                .deleteByFreelancer(savedFreelancer);


        if (skillIds != null) {

            for (Long skillId : skillIds) {

                Skill skill =
                        skillRepository.findById(skillId)
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


        return "redirect:/freelancer/profile";
    }


    // =========================================================
    // OLD CREATE SKILL PAGE
    // =========================================================

    @GetMapping("/freelancer/skill")
    public String showSkillForm(Model model) {

        model.addAttribute(
                "skill",
                new Skill()
        );

        return "freelancer/skillform";
    }


    // =========================================================
    // SAVE NEW SKILL
    // =========================================================

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
                        .findByNameIgnoreCase(skillName);


        if (existingSkill.isEmpty()) {

            Skill newSkill =
                    new Skill();

            newSkill.setName(skillName);

            skillRepository.save(newSkill);
        }


        // After creating the skill,
        // return to the freelancer form.

        return "redirect:/freelancer/edit";
    }


    // =========================================================
    // EDIT FREELANCER
    // =========================================================

    @GetMapping("/freelancer/edit")
    public String editFreelancer(Model model) {


        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        User user =
                userRepository
                        .findByEmail(auth.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        Optional<Freelancer> optionalFreelancer =
                freelancerRepository
                        .findByUser(user);


        // =====================================================
        // NO PROFILE YET
        // =====================================================

        if (optionalFreelancer.isEmpty()) {

            model.addAttribute(
                    "freelancer",
                    new Freelancer()
            );

            model.addAttribute(
                    "skills",
                    skillRepository.findAll()
            );

            model.addAttribute(
                    "selectedSkillIds",
                    new HashSet<Long>()
            );

            // IMPORTANT
            // Needed for popup

            model.addAttribute(
                    "skill",
                    new Skill()
            );


            return "freelancer/freelancer-create";
        }


        // =====================================================
        // EXISTING PROFILE
        // =====================================================

        Freelancer freelancer =
                optionalFreelancer.get();


        Set<Long> selectedSkillIds =
                new HashSet<>();


        for (FreelancerSkill freelancerSkill :
                freelancer.getFreelancerSkills()) {

            selectedSkillIds.add(
                    freelancerSkill
                            .getSkill()
                            .getId()
            );
        }


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


        // IMPORTANT
        // Needed for popup

        model.addAttribute(
                "skill",
                new Skill()
        );


        return "freelancer/freelancer-create";
    }
    
    
    @GetMapping("/freelancers")
    public String freelancers(Model model) {

        List<Freelancer> freelancers =
                freelancerService.getAllFreelancers();

        model.addAttribute("freelancers", freelancers);

        return "freelancer/freelancerHome";
    }
    
    @GetMapping("/freelancer-details/{id}")
    public String freelancerDetails(
            @PathVariable Long id,
            Model model) {

        Freelancer freelancer =
                freelancerService.getFreelancerById(id);

        model.addAttribute("freelancer", freelancer);

        return "freelancer/freelancer-details";
    }
    
   
}