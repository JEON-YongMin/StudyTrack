package com.studytrack.study.entity;

import com.studytrack.study.enums.StudyCategory;
import jakarta.persistence.*;

@Entity
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

    @Column(nullable = false, unique = true, length = 20)
    private String inviteCode;

    protected Study() {}

    public Study(String title, String description,Integer memberCount, Integer maxMembers, StudyCategory category, String inviteCode) {
        this.title = title;
        this.description = description;
        this.memberCount = 1;
        this.maxMembers = maxMembers;
        this.category = category;
        this.inviteCode = inviteCode;
    }

    public Long getId() { return id; }
    public String getInviteCode() { return inviteCode; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getMaxMembers() { return maxMembers; }
    public StudyCategory getCategory() { return category; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

    public void increaseMemberCount() {
        this.memberCount++;
    }

    public void decreaseMemberCount() {
        if (this.memberCount > 0) this.memberCount--;
    }
}
