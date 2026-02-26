package com.studytrack.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/user/mypage")
    public String myPage() {
        return "user/mypage";
    }

    @GetMapping("/user/edit_profile")
    public String editProfile() {
        return "user/edit_profile";
    }
}