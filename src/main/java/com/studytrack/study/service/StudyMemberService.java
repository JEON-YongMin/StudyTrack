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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public StudyMember getOwnerMember(Long studyId) {
        return studyMemberRepository.findByStudyIdAndRole(studyId, StudyRole.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("스터디 OWNER를 찾을 수 없습니다. studyId=" + studyId));
    }

    @Transactional
    public void join(Long studyId, String userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        boolean alreadyJoined = studyMemberRepository.existsByStudyAndUser(study, user);
        if (alreadyJoined) {
            throw new IllegalStateException("이미 참여 중이거나 신청한 스터디입니다.");
        }

        if (study.getMemberCount() >= study.getMaxMembers()) {
            throw new IllegalStateException("스터디 정원이 가득 찼습니다.");
        }

        StudyMember newMember = StudyMember.builder()
                .study(study)
                .user(user)
                .nickname(user.getNickname())
                .role(StudyRole.MEMBER)
                .applicationAt(LocalDateTime.now())
                .status(Status.WAITING)
                .isInvited(false)
                .build();

        studyMemberRepository.save(newMember);
    }

    @Transactional
    public void updateStatus(Long studyId, Long memberId, String status) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
        StudyMember member = studyMemberRepository.findByStudy_IdAndId(studyId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));

        if (status.equals("APPROVED")) {
            if (member.getStatus() != Status.APPROVED) {
                member.setStatus(Status.APPROVED);
                member.setJoinedAt(LocalDateTime.now());
                study.increaseMemberCount();
            }
        } else if (status.equals("REJECTED")) {
            member.setStatus(Status.REJECTED);
        }
    }

    @Transactional
    public void updateRole(Long studyId, Long memberId, String roleName) {
        StudyMember member = studyMemberRepository.findByStudy_IdAndId(studyId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));
        member.setRole(StudyRole.valueOf(roleName));
    }

    @Transactional
    public void removeMember(Long memberId) {
        studyMemberRepository.deleteById(memberId);
    }

    public List<StudyMember> searchMembers(Long studyId, String nickname, String roleFilter, String statusFilter) {
        List<StudyMember> members = studyMemberRepository.findByStudyId(studyId);

        if (nickname != null && !nickname.isEmpty()) {
            members = members.stream()
                    .filter(m -> m.getNickname().toLowerCase().contains(nickname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (roleFilter != null && !roleFilter.isEmpty()) {
            members = members.stream()
                    .filter(m -> m.getRole().name().equals(roleFilter))
                    .collect(Collectors.toList());
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            members = members.stream()
                    .filter(m -> m.getStatus().name().equals(statusFilter))
                    .collect(Collectors.toList());
        }

        return members;
    }

    @Transactional
    public void inviteMember(Long studyId, String inviteeNickname, String inviterUserId) {
        // 1. 스터디 정보 가져오기
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다."));

        // 2. 초대자(방장) 정보 가져오기
        User inviter = userRepository.findByUserId(inviterUserId)
                .orElseThrow(() -> new IllegalArgumentException("초대자를 찾을 수 없습니다."));

        // 3. [중요] 화면에서 넘어온 '닉네임'으로 초대받을 유저의 '진짜 정보' 찾기
        User invitee = userRepository.findByNickname(inviteeNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임을 가진 사용자를 찾을 수 없습니다."));

        // 4. 방장 권한 체크
        StudyMember owner = getOwnerMember(studyId);
        if (!owner.getUser().getId().equals(inviter.getId())) {
            throw new IllegalStateException("방장만 멤버를 초대할 수 있습니다.");
        }

        // 5. 중복 초대 체크 (조회된 사용자의 진짜 UserId 사용)
        String realInviteeId = invitee.getUserId(); // 여기서 'hello'가 추출됨
        boolean alreadyWaiting = studyMemberRepository.existsByStudyIdAndUserUserIdAndStatus(
                studyId, realInviteeId, Status.WAITING
        );
        if (alreadyWaiting) {
            throw new IllegalStateException("이미 초대된 사용자입니다.");
        }

        // 6. 저장 (User 객체를 통째로 넣으면 JPA가 알아서 ID를 매핑함)
        StudyMember inviteMember = StudyMember.builder()
                .study(study)
                .user(invitee) // invitee 객체 안에는 ID 'hello'가 들어있음
                .nickname(invitee.getNickname())
                .role(StudyRole.MEMBER)
                .applicationAt(LocalDateTime.now())
                .status(Status.WAITING)
                .isInvited(true)
                .build();

        System.out.println("데이터 저장 직전!");
        studyMemberRepository.save(inviteMember);
    }

    @Transactional(readOnly = true)
    public List<StudyMember> getWaitingInvites(String userId) {
        return studyMemberRepository.findWaitingInvitesWithStudy(userId, Status.WAITING);
    }

    @Transactional
    public void acceptInvite(Long studyMemberId, String userId) {
        StudyMember studyMember = studyMemberRepository.findByIdAndUserUserId(studyMemberId, userId)
                .orElseThrow(() -> new IllegalArgumentException("초대를 찾을 수 없습니다."));

        if (studyMember.getStatus() != Status.WAITING) {
            throw new IllegalStateException("이미 처리된 초대입니다.");
        }

        Study study = studyMember.getStudy();

        if (study.getMemberCount() >= study.getMaxMembers()) {
            throw new IllegalStateException("스터디 정원이 가득 찼습니다.");
        }

        studyMember.setStatus(Status.APPROVED);
        studyMember.setJoinedAt(LocalDateTime.now());
        study.increaseMemberCount();
    }

    @Transactional
    public void rejectInvite(Long studyMemberId, String userId) {
        StudyMember studyMember = studyMemberRepository.findByIdAndUserUserId(studyMemberId, userId)
                .orElseThrow(() -> new IllegalArgumentException("초대를 찾을 수 없습니다."));

        if (studyMember.getStatus() != Status.WAITING) {
            throw new IllegalStateException("이미 처리된 초대입니다.");
        }

        studyMember.setStatus(Status.REJECTED);
    }
}