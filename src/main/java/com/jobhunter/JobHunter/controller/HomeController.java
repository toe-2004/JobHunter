package com.jobhunter.JobHunter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String home() {
        return "index";
    }
}
