package com.studytrack.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudyDetailController {

    @GetMapping("/study/detail")
    public String studyDetail() {
        return "study/studyDetail";
    }

    @GetMapping("/notice/detail")
    public String noticeDetail() {
        return "study/noticeDetail";
    }

    @GetMapping("/assignment/create")
    public String assignmentCreate() {
        return "study/assignmentCreate";
    }

    @GetMapping("/study/setting")
    public String studySetting() {
        return "study/studySetting";
    }

    @GetMapping("/study/members")
    public String studyMembers() {
        return "study/studyMembers";
    }

}
