package com.studytrack.study.repository;

import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.entity.Study;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    Optional<StudyMember> findByStudyAndUser(Study study, User user);

    Optional<StudyMember> findByStudyIdAndRole(Long studyId, StudyRole role);

    long countByStudy(Study study);
}