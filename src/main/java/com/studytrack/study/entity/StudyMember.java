package com.studytrack.study.entity;

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

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    protected StudyMember() {}

    public StudyMember(Study study, User user, String nickname, StudyRole role) {
        this.study = study;
        this.user = user;
        this.nickname = nickname;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }
}