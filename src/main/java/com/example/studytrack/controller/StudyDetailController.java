package com.example.studytrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudyDetailController {

    @GetMapping("/study/detail")
    public String studyMain() {
        return "study/studyDetail";
    }

}
