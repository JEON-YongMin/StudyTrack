package com.studytrack.study.dto;

import com.studytrack.study.enums.StudyCategory;

public record StudyResponseDto(
        Long id,
        String title,
        String description,
        StudyCategory category,
        int memberCount,
        Integer maxMembers,
        String ownerName,
        boolean full
) {}
