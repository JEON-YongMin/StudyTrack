package com.studytrack.study.dto;

import com.studytrack.study.enums.StudyCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class StudyCreateDto {

    @Schema(description = "스터디 개설 요청")
    public record StudyCreateRequest(
            @Schema(description="스터디 제목", example="스프링 부트 스터디")
            @NotBlank String title,

            @Schema(description="스터디 설명", example="매주 화요일 2시간 진행")
            @NotBlank String description,

            @Schema(description="참여인원", example="1")
            Integer memberCount,

            @Schema(description="최대 인원(2~50)", example="5")
            @NotNull @Min(2) @Max(50) Integer maxMembers,

            @Schema(description="카테고리", example="PROJECT")
            @NotNull StudyCategory category
    ) {}

    @Schema(description = "스터디 개설 응답")
    public record StudyCreateResponse(
            @Schema(description="스터디 ID", example="1")
            Long studyId,

            @Schema(description="초대 코드", example="STUDY-1234")
            String inviteCode
    ) {}

}