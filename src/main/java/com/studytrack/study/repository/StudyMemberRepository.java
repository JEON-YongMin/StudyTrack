package com.studytrack.study.repository;

import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.entity.Study;
import com.studytrack.study.enums.Status;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    Optional<StudyMember> findByStudyAndUser(Study study, User user);

    Optional<StudyMember> findByStudyIdAndRole(Long studyId, StudyRole role);

    Optional<StudyMember> findByStudy_IdAndUser_UserId(Long studyId, String userId);

    Optional<StudyMember> findByStudy_IdAndId(Long studyId, Long memberId);

    Optional<StudyMember> findByIdAndUserUserId(Long id, String userId);

    boolean existsByStudyIdAndUserUserId(Long studyId, String userId);

    @Query("select sm from StudyMember sm join fetch sm.study where sm.user.userId = :userId")
    List<StudyMember> findByUserIdWithStudy(@Param("userId") String userId);

    @Query("""
       select sm
       from StudyMember sm
       join fetch sm.study
       where sm.user.userId = :userId
         and sm.status = :status
         and sm.isInvited = true 
       order by sm.applicationAt desc
       """)
    List<StudyMember> findWaitingInvitesWithStudy(@Param("userId") String userId,
                                                  @Param("status") Status status);

    boolean existsByStudyAndUser(Study study, User user);

    boolean existsByStudyIdAndUserUserIdAndStatus(Long studyId, String userId, Status status);

    List<StudyMember> findByStudyId(Long studyId);

    long countByStudy(Study study);
}