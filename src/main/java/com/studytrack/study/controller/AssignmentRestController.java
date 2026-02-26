package com.studytrack.study.controller;

import com.studytrack.study.dto.AssignmentDto;
import com.studytrack.study.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/{studyId}/assignments")
public class AssignmentRestController {

    private final AssignmentService assignmentService;

    @PostMapping
    public AssignmentDto.Response create(@PathVariable Long studyId,
                                         @Valid @RequestBody AssignmentDto.CreateRequest req) {
        return assignmentService.create(studyId, req);
    }

    @GetMapping
    public List<AssignmentDto.Response> list(@PathVariable Long studyId) {
        return assignmentService.list(studyId);
    }

    @PutMapping("/{assignmentId}")
    public AssignmentDto.Response update(@PathVariable Long studyId,
                                         @PathVariable Long assignmentId,
                                         @Valid @RequestBody AssignmentDto.Request req) {
        return assignmentService.updateAssignment(studyId, assignmentId, req);
    }

    @DeleteMapping("/{assignmentId}")
    public void delete(@PathVariable Long studyId,
                       @PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(studyId, assignmentId);
    }
}