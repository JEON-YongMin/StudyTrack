package com.studytrack.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/auth/signup")
    public String signupPage() {
        return "auth/signup";
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }
}