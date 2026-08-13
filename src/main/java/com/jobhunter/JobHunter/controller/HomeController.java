package com.jobhunter.JobHunter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import com.jobhunter.JobHunter.model.User;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {

    @GetMapping("/index")
    public String home() {
        return "login";
    }
    
    // @GetMapping("/login")
    // public String login() {
    //     return "login";
    // }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

}
