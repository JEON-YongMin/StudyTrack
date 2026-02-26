package com.studytrack.study.repository;

import com.studytrack.study.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByStudyIdOrderByCreatedAtDesc(Long studyId);

    Optional<Assignment> findByIdAndStudyId(Long id, Long studyId);

}