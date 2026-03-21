package com.studytrack.study.controller;

import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.Status;
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
                               @RequestParam(value = "appQ", required = false) String appNickname,
                               @RequestParam(value = "roleFilter", required = false) String roleFilter,
                               @RequestParam(value = "statusFilter", defaultValue = "WAITING") String statusFilter,
                               Model model) {

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        List<StudyMember> members = studyMemberService.searchMembers(studyId, nickname, roleFilter, "APPROVED");

        List<StudyMember> waitingMembers = studyMemberService.searchMembers(studyId, appNickname, null, statusFilter);

        int waitingCount = studyMemberService.searchMembers(studyId, appNickname, null, "WAITING").size();

        StudyMember myInfo = studyMemberRepository.findByStudy_IdAndUser_UserId(studyId, userId).orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        model.addAttribute("study", study);
        model.addAttribute("members", members);
        model.addAttribute("memberCount", members.size());
        model.addAttribute("q", nickname);
        model.addAttribute("roleFilter", roleFilter);
        model.addAttribute("appQ", appNickname);
        model.addAttribute("waitingMembers", waitingMembers);
        model.addAttribute("waitingCount", waitingCount);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("myInfo", myInfo);

        return "study/studyMembers";

    }

    @PostMapping("/{memberId}/role")
    public String changeMemberRole(@PathVariable("studyId") Long studyId,
                                   @PathVariable("memberId") Long memberId,
                                   @RequestParam("role") String role,
                                   @RequestParam("userId") String userId) {

        studyMemberService.updateRole(studyId, memberId, role);

        return "redirect:/study/" + studyId + "/members?userId=" + userId;
    }

    @PostMapping("/{memberId}/kick")
    public String kickMember(@PathVariable("studyId") Long studyId,
                             @PathVariable("memberId") Long memberId,
                             @RequestParam("userId") String userId) {

        studyMemberService.removeMember(memberId);

        return "redirect:/study/" + studyId + "/members?userId=" + userId;
    }

    @PostMapping("/{memberId}/approve")
    public String approveMember(@PathVariable("studyId") Long studyId,
                                @PathVariable("memberId") Long memberId,
                                @RequestParam String userId) {

        studyMemberService.updateStatus(studyId, memberId, "APPROVED");

        return "redirect:/study/" + studyId + "/members?userId=" + userId + "&tab=approval";
    }

    // 가입 거절 처리
    @PostMapping("/{memberId}/reject")
    public String rejectMember(@PathVariable("studyId") Long studyId,
                               @PathVariable("memberId") Long memberId,
                               @RequestParam String userId) {

        studyMemberService.updateStatus(studyId, memberId, "REJECTED");

        return "redirect:/study/" + studyId + "/members?userId=" + userId + "&tab=approval";
    }
}
