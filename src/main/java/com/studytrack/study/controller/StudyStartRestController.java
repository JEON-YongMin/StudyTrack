package com.studytrack.study.controller;

import com.studytrack.study.dto.StudyResponseDto;
import com.studytrack.study.enums.StudyCategory;
import com.studytrack.study.service.StudyMemberService;
import com.studytrack.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/join")
public class StudyStartRestController {

    private  final StudyMemberService studyMemberService;

    @PostMapping("/{studyId}")
    @ResponseBody
    public ResponseEntity<?> joinStudy(@PathVariable Long studyId, @RequestBody Map<String, String> requestData) {
        try {
            String userId = requestData.get("userId"); // JSON에서 userId 추출

            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "사용자 ID가 필요합니다."));
            }

            studyMemberService.join(studyId, userId);

            // 성공 시 JSON 반환
            return ResponseEntity.ok().body(Map.of("message", "성공적으로 참여되었습니다."));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/invite")
    @ResponseBody
    public ResponseEntity<?> joinByInviteCode(@RequestBody Map<String, String> requestData) {
        try {
            String inviteCodeInput = requestData.get("inviteCodeInput");
            String userId = requestData.get("userId");

            Long studyId = studyMemberService.joinByInviteCode(inviteCodeInput, userId);

            return ResponseEntity.ok().body(Map.of("message", "초대 코드로 참여에 성공했습니다!", "studyId", studyId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
