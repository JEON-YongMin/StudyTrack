package com.studytrack.study.service;

import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.study.repository.StudyMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;

    public StudyMemberService(StudyMemberRepository studyMemberRepository) {
        this.studyMemberRepository = studyMemberRepository;
    }

    public StudyMember getOwnerMember(Long studyId) {
        return studyMemberRepository.findByStudyIdAndRole(studyId, StudyRole.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("스터디 OWNER를 찾을 수 없습니다. studyId=" + studyId));
    }

}
