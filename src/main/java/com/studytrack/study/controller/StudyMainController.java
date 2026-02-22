package com.studytrack.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudyMainController {

    @GetMapping("/")
    public String studyMain() {
        return "studyMain/studyMain";
    }

    @GetMapping("/login")
    public String login() {
        return "studyMain/studyDashboard";
    }

}
