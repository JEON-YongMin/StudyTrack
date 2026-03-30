package com.studytrack.study.entity;

import com.studytrack.study.enums.Status;
import com.studytrack.study.enums.StudyRole;
import com.studytrack.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyRole role;

    @Column
    private LocalDateTime joinedAt;

    @Column(nullable = false)
    private LocalDateTime applicationAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // [추가] 초대 여부 플래그: true(방장이 초대함), false(유저가 신청함)
    @Column(nullable = false)
    @Builder.Default
    private boolean isInvited = false;

    protected StudyMember() {}

    // 생성자 업데이트
    public StudyMember(Study study, User user, String nickname, StudyRole role, LocalDateTime joinedAt, Status status, boolean isInvited) {
        this.study = study;
        this.user = user;
        this.nickname = nickname;
        this.role = role;
        this.joinedAt = joinedAt;
        this.applicationAt = LocalDateTime.now();
        this.status = status;
        this.isInvited = isInvited;
    }
}