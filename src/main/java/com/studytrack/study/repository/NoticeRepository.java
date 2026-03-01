package com.studytrack.study.repository;

import com.studytrack.study.entity.Notice;
import com.studytrack.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByStudyIdOrderByCreatedAtDesc(Long studyId);

    Optional<Notice> findByIdAndStudyId(Long id, Long studyId);

}