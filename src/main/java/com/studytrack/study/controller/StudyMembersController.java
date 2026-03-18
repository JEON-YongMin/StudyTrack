package com.studytrack.study.controller;

import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}/members")
public class StudyMembersController {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyMemberService studyMemberService;

    @GetMapping
    public String studyMembers(@PathVariable Long studyId,
                               @RequestParam(value = "userId", required = false) String userId,
                               @RequestParam(value = "q", required = false) String nickname,
                               @RequestParam(value = "roleFilter", required = false) String roleFilter,
                               Model model) {

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        List<StudyMember> members = studyMemberService.searchMembers(studyId, nickname, roleFilter);

        StudyMember myInfo = studyMemberRepository.findByStudy_IdAndUser_UserId(studyId, userId).orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        model.addAttribute("study", study);
        model.addAttribute("members", members);
        model.addAttribute("memberCount", members.size());
        model.addAttribute("q", nickname);
        model.addAttribute("roleFilter", roleFilter);
        model.addAttribute("myInfo", myInfo);

        return "study/studyMembers";

    }

    @PostMapping("/{memberId}/role")
    public String changeMemberRole(@PathVariable("studyId") Long studyId,
                                   @PathVariable("memberId") Long memberId,
                                   @RequestParam("role") String role,
                                   @RequestParam("userId") String userId) {
        // 1. 보안 체크: 요청을 보낸 유저(userId)가 방장(OWNER)이나 매니저인지 확인하는 로직 권장

        // 2. 역할 업데이트 서비스 호출
        studyMemberService.updateRole(memberId, role);

        // 3. 작업 후 다시 멤버 관리 페이지로 리다이렉트 (userId 유지)
        return "redirect:/study/" + studyId + "/members?userId=" + userId;
    }

    @PostMapping("/{memberId}/kick")
    public String kickMember(@PathVariable("studyId") Long studyId,
                             @PathVariable("memberId") Long memberId,
                             @RequestParam("userId") String userId) {
        // 1. 보안 체크: 본인이 본인을 내보내거나 방장을 내보내지 못하도록 로직 필요

        // 2. 멤버 삭제 서비스 호출
        studyMemberService.removeMember(memberId);

        // 3. 리다이렉트 (userId 유지)
        return "redirect:/study/" + studyId + "/members?userId=" + userId;
    }
}
