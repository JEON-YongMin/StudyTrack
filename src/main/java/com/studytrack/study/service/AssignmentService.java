package com.studytrack.study.service;

import com.studytrack.study.dto.AssignmentDto;
import com.studytrack.study.entity.Assignment;
import com.studytrack.study.entity.Study;
import com.studytrack.study.enums.Progress;
import com.studytrack.study.repository.AssignmentRepository;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    public AssignmentDto.Response create(Long studyId, AssignmentDto.CreateRequest req) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        // ✅ 로그인 유저
        String userId = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        // ✅ 스터디 멤버인지 확인
        studyMemberRepository.findByStudyAndUser(study, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "스터디 멤버만 과제를 추가할 수 있습니다."));


        Assignment saved = assignmentRepository.save(
                new Assignment(study, req.title(), req.description(), req.priority(), Progress.INPROGRESS, req.dueDate())
        );

        return new AssignmentDto.Response(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getPriority(),
                saved.getProgress(),
                saved.getDueDate(),
                saved.getCreatedAt()
        );
    }

    @Transactional
    public AssignmentDto.Response updateAssignment(Long studyId, Long assignmentId, AssignmentDto.Request form) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("과제를 찾을 수 없습니다. id=" + assignmentId));

        // (선택) 보안/정합성: 해당 과제가 그 스터디 소속인지 체크
        if (!assignment.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("스터디에 속하지 않은 과제입니다.");
        }

        assignment.update(
                form.title(),
                form.description(),
                form.priority(),
                form.dueDate()
        );

        // save 호출은 선택(영속 상태면 트랜잭션에서 flush됨). 명확히 하려면 save 해도 OK
        // assignmentRepository.save(assignment);

        return toResponse(assignment);
    }

    @Transactional
    public void deleteAssignment(Long studyId, Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("과제를 찾을 수 없습니다. id=" + assignmentId));

        if (!assignment.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("해당 스터디의 과제가 아닙니다. studyId=" + studyId);
        }

        assignmentRepository.delete(assignment);
    }

    @Transactional(readOnly = true)
    public List<AssignmentDto.Response> list(Long studyId) {
        return assignmentRepository.findByStudyIdOrderByCreatedAtDesc(studyId).stream()
                .map(a -> new AssignmentDto.Response(
                        a.getId(), a.getTitle(), a.getDescription(), a.getPriority(), a.getProgress(), a.getDueDate(), a.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public AssignmentDto.Response getAssignment(Long studyId, Long assignmentId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("과제를 찾을 수 없습니다."));


        if (!assignment.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("해당 스터디의 과제가 아닙니다.");
        }

        return new AssignmentDto.Response(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getPriority(),
                assignment.getProgress(),
                assignment.getDueDate(),
                assignment.getCreatedAt()
        );
    }

    private AssignmentDto.Response toResponse(Assignment assignment) {
        return new AssignmentDto.Response(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getPriority(),
                assignment.getProgress(),
                assignment.getDueDate(),
                assignment.getCreatedAt()
        );
    }
}