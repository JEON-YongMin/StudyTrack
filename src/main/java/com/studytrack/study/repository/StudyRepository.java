package com.studytrack.study.repository;

import com.studytrack.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
    boolean existsByInviteCode(String inviteCode);
}
