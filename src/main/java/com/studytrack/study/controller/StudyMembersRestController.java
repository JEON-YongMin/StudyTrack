package com.studytrack.study.controller;

import com.studytrack.auth.jwt.JwtProvider;
import com.studytrack.study.dto.StudyMemberDto;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/{studyId}/members")
public class StudyMembersRestController {

    private final JwtProvider jwtProvider;
    private final StudyMemberService studyMemberService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyStudyMember(@PathVariable Long studyId,
                                              @RequestHeader("Authorization") String token) {

        String userId = jwtProvider.getUserId(token);

        StudyMember member = studyMemberService.getMember(studyId, userId);

        StudyMemberDto response = new StudyMemberDto(
                member.getUser(),
                member.getNickname(),
                member.getRole()
        );

        return ResponseEntity.ok(response); // Role 정보를 포함한 객체 반환
    }
}
