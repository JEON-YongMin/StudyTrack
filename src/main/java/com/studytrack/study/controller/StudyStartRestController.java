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

    /**
     * ✅ 이미지 3 관련: 초대 코드로 스터디 가입 처리
     * POST /api/study/join/code?inviteCode=STUDY-XXXXXX
     */
    @PostMapping("/join/code")
    public ResponseEntity<String> joinByInviteCode(@RequestParam String inviteCode) {

        // 1. 현재 로그인한 사용자 ID 추출 (SecurityContext 이용)
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = String.valueOf(principal);

        if (userId == null || userId.equals("anonymousUser")) {
            return ResponseEntity.status(401).body("로그인이 필요한 서비스입니다.");
        }

        try {
            // 2. 서비스 로직 호출 (코드 검증 -> 정원 확인 -> 멤버 등록)
            studyService.joinByInviteCode(inviteCode, userId);
            return ResponseEntity.ok("스터디 참여에 성공했습니다!");

        } catch (IllegalArgumentException e) {
            // 잘못된 코드인 경우
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // 정원이 찼거나 이미 가입된 경우
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }
}