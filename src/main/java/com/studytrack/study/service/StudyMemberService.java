package com.studytrack.study.service;

import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.Status;
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

import java.util.List;
import java.util.stream.Collectors;

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
                .applicationAt(LocalDateTime.now())
                .status(Status.WAITING)
                .build();

        studyMemberRepository.save(newMember);

    }

    @Transactional
    public void updateStatus(Long studyId, Long memberId, String status){

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
        StudyMember member = studyMemberRepository.findByStudy_IdAndId(studyId, memberId).orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));

        if (status.equals("APPROVED")) {
            member.setStatus(Status.APPROVED);
            study.increaseMemberCount();
        }else if (status.equals("REJECTED")) {
            member.setStatus(Status.REJECTED);
        }

    }

    @Transactional
    public void updateRole(Long studyId, Long memberId, String roleName) {
        StudyMember member = studyMemberRepository.findByStudy_IdAndId(studyId, memberId).orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));

        // String을 Enum(Role)으로 변환하여 설정
        member.setRole(StudyRole.valueOf(roleName));
    }

    @Transactional
    public void removeMember(Long memberId) {
        studyMemberRepository.deleteById(memberId);
    }


    public List<StudyMember> searchMembers(Long studyId, String nickname, String roleFilter, String statusFilter) {
        // 1. 해당 스터디의 전체 멤버를 먼저 가져옴
        List<StudyMember> members = studyMemberRepository.findByStudyId(studyId);

        // 2. 닉네임 검색어 필터링 (case-insensitive)
        if (nickname != null && !nickname.isEmpty()) {
            members = members.stream()
                    .filter(m -> m.getNickname().toLowerCase().contains(nickname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // 3. 역할(Role) 필터링
        if (roleFilter != null && !roleFilter.isEmpty()) {
            members = members.stream()
                    .filter(m -> m.getRole().name().equals(roleFilter))
                    .collect(Collectors.toList());
        }

        // 4. 역할(status) 필터링
        if (statusFilter != null && !statusFilter.isEmpty()) {
            members = members.stream()
                    .filter(m -> m.getStatus().name().equals(statusFilter))
                    .collect(Collectors.toList());
        }

        return members;
    }

}
