package com.studytrack.study.controller;

import com.studytrack.study.service.StudyMemberService;
import com.studytrack.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/join")
public class StudyStartRestController {

    private final StudyMemberService studyMemberService;
    private final StudyService studyService;

    /**
     * 공개 스터디 참여
     * POST /api/study/join/{studyId}
     */
    @PostMapping("/{studyId}")
    public ResponseEntity<?> joinStudy(
            @PathVariable Long studyId,
            @RequestBody Map<String, String> requestData
    ) {
        try {
            String userId = requestData.get("userId");

            if (userId == null || userId.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "사용자 ID가 필요합니다."));
            }

            studyMemberService.join(studyId, userId);

            return ResponseEntity.ok(
                    Map.of("message", "참여 승인 요청이 전달되었습니다.")
            );

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * 초대 코드로 스터디 참여
     * POST /api/study/join/code?inviteCode=STUDY-XXXXXX
     */
    @PostMapping("/code")
    public ResponseEntity<String> joinByInviteCode(@RequestParam String inviteCode) {

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String userId = String.valueOf(principal);

        if (userId == null || userId.isBlank() || userId.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        try {
            studyService.joinByInviteCode(inviteCode, userId);
            return ResponseEntity.ok("스터디 참여에 성공했습니다!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }
}