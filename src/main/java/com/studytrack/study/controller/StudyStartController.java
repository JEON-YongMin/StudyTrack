package com.studytrack.study.controller;

import com.studytrack.study.dto.StudyResponseDto;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.StudyCategory;
import com.studytrack.study.service.StudyMemberService;
import com.studytrack.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyStartController {

    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    @GetMapping("/study/start")
    public String studyStart(@RequestParam(value = "userId", required = false) String userId,
                             @RequestParam(value = "q", required = false) String query,
                             @RequestParam(value = "category", required = false) StudyCategory category,
                             @PageableDefault(size = 10) Pageable pageable,
                             Model model) {

        List<StudyResponseDto> myStudies = studyService.getMyStudies(userId);
        Page<StudyResponseDto> publicStudies = studyService.findPublicStudies(query, category, pageable);
        List<StudyMember> receivedInvites = studyMemberService.getWaitingInvites(userId);

        model.addAttribute("myStudies", myStudies);
        model.addAttribute("myStudiesCount", myStudies.size());

        model.addAttribute("publicStudies", publicStudies);
        model.addAttribute("totalPublicCount", publicStudies.getTotalElements());

        model.addAttribute("receivedInvites", receivedInvites);
        model.addAttribute("receivedInvitesCount", receivedInvites.size());

        model.addAttribute("q", query);
        model.addAttribute("category", category);
        model.addAttribute("userId", userId);

        return "study/studyStart";
    }
}