package com.studytrack.study.controller;

import com.studytrack.study.dto.AssignmentDto;
import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.AssignmentRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.AssignmentService;
import com.studytrack.study.service.StudyMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AssignmentController {

    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;
    private final AssignmentService assignmentService;

    public AssignmentController(StudyRepository studyRepository, StudyMemberService studyMemberService, AssignmentService assignmentService) {
        this.studyRepository = studyRepository;
        this.studyMemberService = studyMemberService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/study/{studyId}/assignments/new")
    public String assignmentCreatePage(@PathVariable Long studyId, Model model) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found"));

        StudyMember owner = studyMemberService.getOwnerMember(studyId);

        model.addAttribute("study", study);

        model.addAttribute("ownerName", owner.getNickname());

        return "study/assignment";
    }

    @GetMapping("/study/{studyId}/assignment/{assignmentId}")
    public String assignmentDetail(@PathVariable Long studyId, @PathVariable Long assignmentId, Model model) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found"));

        AssignmentDto.Response assignment = assignmentService.getAssignment(studyId, assignmentId);
        StudyMember owner = studyMemberService.getOwnerMember(studyId);

        model.addAttribute("study", study);
        model.addAttribute("ownerName", owner.getNickname());
        model.addAttribute("assignment", assignment);
        model.addAttribute("mode", "detail");

        return "study/assignment";
    }

}
