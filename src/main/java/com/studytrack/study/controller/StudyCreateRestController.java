package com.studytrack.study.controller;

import com.studytrack.study.dto.StudyCreateDto;
import com.studytrack.study.dto.StudyDetailDto;
import com.studytrack.study.entity.Study;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.StudyCreateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Study API", description = "스터디 개설/조회/설정")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/studyCreate")
public class StudyCreateRestController {

    private final StudyCreateService studyCreateService;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    public StudyCreateRestController(StudyCreateService studyCreateService, StudyRepository studyRepository, StudyMemberRepository studyMemberRepository) {
        this.studyCreateService = studyCreateService;
        this.studyRepository = studyRepository;
        this.studyMemberRepository = studyMemberRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public StudyCreateDto.StudyCreateResponse create(@Valid @RequestBody StudyCreateDto.StudyCreateRequest request) {
        return studyCreateService.create(request); // ✅ 더미 말고 실제 저장
    }

    @Operation(summary = "스터디 상세 조회", description = "studyId로 스터디 상세를 조회합니다.")
    @GetMapping("/{studyId}")
    public StudyDetailDto.StudyDetailResponse detail(@PathVariable Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        return new StudyDetailDto.StudyDetailResponse(
                study.getId(),
                study.getTitle(),
                study.getDescription(),
                study.getMaxMembers(),
                study.getCategory(),
                study.getInviteCode(),
                study.getMemberCount()
        );
    }

    @GetMapping("/{studyId}/members/count")
    public long memberCount(@PathVariable Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return studyMemberRepository.countByStudy(study);
    }
}