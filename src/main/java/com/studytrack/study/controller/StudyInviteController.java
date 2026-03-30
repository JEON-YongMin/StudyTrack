package com.studytrack.study.controller;

import com.studytrack.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class StudyInviteController {

    private final StudyMemberService studyMemberService;

    @PostMapping("/study/{studyId}/invite")
    public String sendInvite(@PathVariable Long studyId,
                             @RequestParam("nickname") String nickname,
                             @RequestParam("inviterId") String inviterId,
                             RedirectAttributes redirectAttributes) {
        // 여기서 서비스를 호출해야 DB에 데이터가 쌓입니다!
        studyMemberService.inviteMember(studyId, nickname, inviterId);
        return "redirect:/study/" + studyId + "/members?userId=" + inviterId;
    }

    @PostMapping("/study/invites/{id}/accept")
    public String acceptInvite(@PathVariable Long id,
                               @RequestParam("userId") String userId,
                               RedirectAttributes redirectAttributes) {
        try {
            studyMemberService.acceptInvite(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "초대를 승인했습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/study/start?tab=received&userId=" + userId;
    }

    @PostMapping("/study/invites/{id}/reject")
    public String rejectInvite(@PathVariable Long id,
                               @RequestParam("userId") String userId,
                               RedirectAttributes redirectAttributes) {
        try {
            studyMemberService.rejectInvite(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "초대를 거절했습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/study/start?tab=received&userId=" + userId;
    }
}