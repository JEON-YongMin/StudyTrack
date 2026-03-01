package com.studytrack.study.controller;

import com.studytrack.study.entity.Assignment;
import com.studytrack.study.entity.Notice;
import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.AssignmentRepository;
import com.studytrack.study.repository.NoticeRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLOutput;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyDetailController {

    private final StudyRepository studyRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudyMemberService studyMemberService;
    private final NoticeRepository noticeRepository;

    @GetMapping("/study/{studyId}")
    public String studyDetail(@PathVariable Long studyId, Model model) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        List<Assignment> assignments = assignmentRepository.findByStudyIdOrderByCreatedAtDesc(studyId);
        List<Notice> notice = noticeRepository.findByStudyIdOrderByCreatedAtDesc(studyId);

        model.addAttribute("study", study);
        model.addAttribute("assignments", assignments);
        model.addAttribute("notice", notice);

        return "study/studyDetail";

    }

    @GetMapping("/study/setting")
    public String studySetting() {
        return "study/studySetting";
    }

    @GetMapping("/study/members")
    public String studyMembers() {
        return "study/studyMembers";
    }

    @GetMapping("/study/start")
    public String studyStart() {
        return "study/studyStart";
    }

}
