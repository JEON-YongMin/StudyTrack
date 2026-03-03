package com.studytrack.study.entity;

import com.studytrack.study.enums.StudyCategory;
import com.studytrack.study.enums.Visibility;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "study")
public class Study {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private int memberCount;

    @Column(nullable = false)
    private Integer maxMembers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StudyCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(nullable = false, unique = true, length = 20)
    private String inviteCode;

    protected Study() {}

    public Study(String title, String description,Integer memberCount, Integer maxMembers, StudyCategory category, Visibility visibility, String inviteCode) {
        this.title = title;
        this.description = description;
        this.memberCount = 1;
        this.maxMembers = maxMembers;
        this.category = category;
        this.visibility = visibility;
        this.inviteCode = inviteCode;
    }

    public void update(String title, String description, int maxMembers, StudyCategory category, Visibility visibility) {
        this.title = title;
        this.description = description;
        this.maxMembers = maxMembers;
        this.category = category;
        this.visibility = visibility;
    }

    public void increaseMemberCount() {
        this.memberCount++;
    }

    public void decreaseMemberCount() {
        if (this.memberCount > 0) this.memberCount--;
    }

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignment = new ArrayList<>();
}
