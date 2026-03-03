package com.studytrack.study.controller;

import com.studytrack.study.dto.StudySettingDto;
import com.studytrack.study.service.StudyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/{studyId}/setting")
public class StudySettingRestController {

    private final StudyService studyService;

    // 스터디 설정 업데이트 (PUT)
    @PutMapping
    public StudySettingDto.Response updateSettings(
            @PathVariable Long studyId,
            @Valid @RequestBody StudySettingDto.Request request) {

        return studyService.updateStudy(studyId, request);
    }
}
