package com.studytrack.study.controller;

import com.studytrack.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyStartRestController {

    private final StudyService studyService;

    @PostMapping("/join/code")
    public ResponseEntity<String> joinByInviteCode(@RequestParam String inviteCode) {
        // 현재 로그인한 사용자 ID 추출
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = String.valueOf(principal);

        studyService.joinByInviteCode(inviteCode, userId);
        return ResponseEntity.ok("스터디 참여에 성공했습니다!");
    }
}