package com.studytrack.study.service;

import com.studytrack.study.dto.StudyCreateDto;
import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.study.repository.StudyMemberRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCreateService {

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