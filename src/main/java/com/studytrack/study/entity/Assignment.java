package com.studytrack.study.entity;

import com.studytrack.study.enums.Priority;
import com.studytrack.study.enums.Progress;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    public Assignment(Study study, String title, String description, Priority priority, Progress progress, LocalDateTime dueDate) {
        this.study = study;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.progress = progress;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title,
                       String description,
                       Priority priority,
                       LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}
