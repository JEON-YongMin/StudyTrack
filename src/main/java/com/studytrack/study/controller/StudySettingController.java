package com.studytrack.study.controller;

import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}")
public class StudySettingController {

    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;

    @GetMapping("/setting")
    public String studySetting( @PathVariable Long studyId, Model model) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        StudyMember ownerName = studyMemberService.getOwnerMember(studyId);

        model.addAttribute("study", study);
        model.addAttribute("ownerName", ownerName.getNickname());

        return "study/studySetting";
    }

}
