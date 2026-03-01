package com.studytrack.study.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class NoticeDto {

    public record Request(
            @NotBlank String title,
            @NotBlank String content
    ) {}

    public record Response(
            Long id,
            Long studyId,
            String title,
            String content,
            String authorId,
            LocalDateTime createdAt
    ) {}
}
