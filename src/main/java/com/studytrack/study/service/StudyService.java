package com.studytrack.study.service;

import com.studytrack.study.dto.StudyCreateDto;
import com.studytrack.study.dto.StudyResponseDto;
import com.studytrack.study.dto.StudySettingDto;
import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.StudyCategory;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;

    private final SecureRandom random = new SecureRandom();
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    public StudyCreateDto.StudyCreateResponse create(StudyCreateDto.StudyCreateRequest request) {
        String inviteCode = generateUniqueInviteCode();

        Study study = new Study(
                request.title(),
                request.description(),
                request.memberCount(),
                request.maxMembers(),
                request.category(),
                request.visibility(),
                inviteCode
        );

        Study savedStudy = studyRepository.save(study);

        // ✅ 개설자(OWNER) 자동 참여
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = String.valueOf(principal);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자를 찾을 수 없습니다."));

        StudyMember owner = new StudyMember(savedStudy, user, user.getNickname(), StudyRole.OWNER);
        studyMemberRepository.save(owner);

        return new StudyCreateDto.StudyCreateResponse(savedStudy.getId(), savedStudy.getInviteCode());
    }

    @Transactional
    public StudySettingDto.Response updateStudy(Long id, StudySettingDto.Request request) {

        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        study.update(
                request.title(),
                request.description(),
                request.maxMembers(),
                request.category(),
                request.visibility()
        );

        return new StudySettingDto.Response(
                study.getId(),
                study.getTitle(),
                study.getDescription(),
                study.getMaxMembers(),
                study.getCategory(),
                study.getVisibility()
        );
    }

    @Transactional
    public void deleteStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다. id=" + studyId));


        studyRepository.delete(study);
    }

    @Transactional(readOnly = true)
    public List<StudyResponseDto> getMyStudies(String userId) {
        return studyRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<StudyResponseDto> findPublicStudies(String query, StudyCategory category, Pageable pageable) {
        // Repository에서 Pageable을 포함한 검색 메서드 호출
        String searchQuery = (query != null && !query.trim().isEmpty()) ? query : null;
        return studyRepository.findPublicStudiesWithOwner(searchQuery, category, pageable);
    }

    @Transactional
    public void joinStudy(Long studyId, String userId) {
        // 1. 스터디 존재 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

        // 2. 정원 확인
        if (study.getMemberCount() >= study.getMaxMembers()) {
            throw new IllegalStateException("스터디 정원이 가득 찼습니다.");
        }

        // 3. 이미 가입된 유저인지 확인
        boolean isAlreadyMember = studyMemberRepository.existsByStudyIdAndUserUserId(studyId, userId);
        if (isAlreadyMember) {
            throw new IllegalStateException("이미 참여 중인 스터디입니다.");
        }

        // 4. 멤버 추가 (기본 역할은 MEMBER)
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        StudyMember newMember = StudyMember.builder()
                .study(study)
                .user(user)
                .role(StudyRole.MEMBER)
                .build();

        studyMemberRepository.save(newMember);

        // 5. 스터디 엔티티의 현재 인원수 업데이트 (필요 시)
        study.increaseMemberCount();
    }

    private String generateUniqueInviteCode() {
        for (int attempt = 0; attempt < 20; attempt++) {
            String code = "STUDY-" + randomString(6);
            if (!studyRepository.existsByInviteCode(code)) return code;
        }
        throw new IllegalStateException("초대 코드 생성에 실패했습니다. 다시 시도해주세요.");
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}