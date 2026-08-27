package com.jobhunter.JobHunter.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.service.UserService;
import org.springframework.security.core.Authentication;
import com.jobhunter.JobHunter.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all-users")
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

   

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    


    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam("photo") MultipartFile photo,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }
        String message = userService.registerUser(user, photo);
        if (!message.equals("success")) {
            model.addAttribute("error", message);
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/choose-role")
    public String chooseRole(Model model) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        return "choose-role";
    }

}