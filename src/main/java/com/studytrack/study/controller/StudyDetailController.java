package com.studytrack.study.controller;

import com.studytrack.study.entity.Assignment;
import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.AssignmentRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.StudyMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLOutput;
import java.util.List;

@Controller
public class StudyDetailController {

    private final StudyRepository studyRepository;
    private final AssignmentRepository assignmentRepository;

    public StudyDetailController(StudyRepository studyRepository, AssignmentRepository assignmentRepository) {
        this.studyRepository = studyRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @GetMapping("/study/{studyId}")
    public String studyDetail(@PathVariable Long studyId, Model model) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        List<Assignment> assignments = assignmentRepository.findByStudyIdOrderByCreatedAtDesc(studyId);

        model.addAttribute("study", study);
        model.addAttribute("assignments", assignments);
        return "study/studyDetail";

    }

    @GetMapping("/notice/detail")
    public String noticeDetail() {
        return "study/noticeDetail";
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
