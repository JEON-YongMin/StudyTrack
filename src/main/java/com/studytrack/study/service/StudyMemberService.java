package com.studytrack.study.service;

import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public StudyMember getOwnerMember(Long studyId) {
        return studyMemberRepository.findByStudyIdAndRole(studyId, StudyRole.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("스터디 OWNER를 찾을 수 없습니다. studyId=" + studyId));
    }

    @Transactional
    public void join(Long studyId, String userId){

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 중복 가입 체크
        boolean alreadyJoined = studyMemberRepository.existsByStudyAndUser(study, user);
        if (alreadyJoined) {
            throw new IllegalStateException("이미 참여 중인 스터디입니다.");
        }

        // 정원 체크
        if (study.getMemberCount() >= study.getMaxMembers()) {
            throw new IllegalStateException("스터디 정원이 가득 찼습니다.");
        }

        // 멤버 생성 및 저장
        StudyMember newMember = StudyMember.builder()
                .study(study)
                .user(user)
                .nickname(user.getNickname()) // 유저의 기본 닉네임 사용
                .role(StudyRole.MEMBER)      // 기본 역할은 MEMBER
                .joinedAt(LocalDateTime.now())
                .build();

        studyMemberRepository.save(newMember);

        // 스터디 현재 인원수 증가
        study.increaseMemberCount();

        studyRepository.save(study);
    }

    @Transactional
    public Long joinByInviteCode(String inviteCode, String userId) {

        Study study = studyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 코드입니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (studyMemberRepository.existsByStudyAndUser(study, user)) {
            throw new IllegalStateException("이미 참여 중인 스터디입니다.");
        }

        if (study.getMemberCount() >= study.getMaxMembers()) {
            throw new IllegalStateException("스터디 정원이 가득 찼습니다.");
        }

        StudyMember newMember = StudyMember.builder()
                .study(study)
                .user(user)
                .nickname(user.getNickname())
                .role(StudyRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();

        studyMemberRepository.save(newMember);

        study.increaseMemberCount();

        return study.getId();
    }

}
