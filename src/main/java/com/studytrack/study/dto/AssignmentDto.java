package com.studytrack.study.dto;

import com.studytrack.study.enums.Priority;
import com.studytrack.study.enums.Progress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AssignmentDto {

    public record CreateRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotNull Priority priority,
            LocalDateTime dueDate
    ) {}

    public record Response(
            Long id,
            String title,
            String description,
            Priority priority,
            Progress progress,
            LocalDateTime dueDate,
            LocalDateTime createdAt
    ) {}

    public record Request(
            @NotBlank String title,
            String description,
            @NotNull LocalDateTime dueDate,
            @NotNull Priority priority
    ) {}
}