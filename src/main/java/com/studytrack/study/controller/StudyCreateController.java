package com.studytrack.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudyCreateController {

    @GetMapping("/study/create")
    public String createStudyPage() {
        return "study/create";
    }
}
