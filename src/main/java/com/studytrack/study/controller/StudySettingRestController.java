package com.studytrack.study.controller;

import com.studytrack.study.dto.StudySettingDto;
import com.studytrack.study.service.StudyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/{studyId}/setting")
public class StudySettingRestController {

    private final StudyService studyService;

    @PostMapping("/invite")
    public ResponseEntity<String> inviteMember(
            @PathVariable Long studyId,
            @RequestParam String nickname) {

        studyService.inviteMemberByNickname(studyId, nickname);
        return ResponseEntity.ok(nickname + "님에게 초대를 보냈습니다.");
    }

    // 스터디 설정 업데이트 (PUT)
    @PutMapping
    public StudySettingDto.Response updateSettings(
            @PathVariable Long studyId,
            @Valid @RequestBody StudySettingDto.Request request) {

        return studyService.updateStudy(studyId, request);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteStudy(@PathVariable Long studyId) {
        try {
            studyService.deleteStudy(studyId);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없거나 실패했습니다.");
        }
    }
}
