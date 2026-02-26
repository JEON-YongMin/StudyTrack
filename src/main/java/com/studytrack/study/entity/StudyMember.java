package com.studytrack.study.entity;

import com.studytrack.study.enums.StudyRole;
import com.studytrack.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    public Long getId() { return id; }
    public Study getStudy() { return study; }
    public User getUser() { return user; }
    public String getNickname() {
        return nickname;
    }
    public StudyRole getRole() { return role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}