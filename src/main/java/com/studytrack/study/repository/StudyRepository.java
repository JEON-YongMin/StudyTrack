package com.studytrack.study.repository;

import com.studytrack.study.dto.StudyResponseDto;
import com.studytrack.study.dto.StudySettingDto;
import com.studytrack.study.entity.Study;
import com.studytrack.study.enums.StudyCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

    @Query("SELECT new com.studytrack.study.dto.StudyResponseDto(" +
            "s.id, s.title, s.description, s.category, s.memberCount, s.maxMembers, " +
            "sm.nickname, " + // ✅ StudyMember에 있는 nickname 필드 직접 참조
            "(s.memberCount >= s.maxMembers)) " +
            "FROM Study s " +
            "JOIN s.members sm " + // Study.java의 members 리스트와 조인
            "WHERE s.visibility = 'PUBLIC' " +
            "AND sm.role = com.studytrack.study.enums.StudyRole.OWNER " + // ✅ Enum 경로 명시
            "AND (:query IS NULL OR s.title LIKE %:query% OR s.description LIKE %:query%) " +
            "AND (:category IS NULL OR s.category = :category)")
    Page<StudyResponseDto> findPublicStudiesWithOwner(@Param("query") String query,
                                                      @Param("category") StudyCategory category,
                                                      Pageable pageable);

    @Query("SELECT new com.studytrack.study.dto.StudyResponseDto(" +
            "s.id, s.title, s.description, s.category, s.memberCount, s.maxMembers, " +
            "owner.nickname, " + // ✅ 해당 스터디의 방장 닉네임 추출
            "(s.memberCount >= s.maxMembers)) " +
            "FROM Study s " +
            "JOIN s.members sm " +    // 현재 로그인한 유저가 멤버인지 확인하기 위한 조인
            "JOIN s.members owner " + // 방장 정보를 가져오기 위한 조인
            "WHERE sm.user.userId = :userId " +
            "AND sm.status = com.studytrack.study.enums.Status.APPROVED " +
            "AND owner.role = com.studytrack.study.enums.StudyRole.OWNER") // ✅ 방장인 멤버만 선택
    List<StudyResponseDto> findByUserId(@Param("userId") String userId);


}
