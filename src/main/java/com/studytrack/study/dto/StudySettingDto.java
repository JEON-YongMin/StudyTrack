package com.studytrack.study.dto;

import com.studytrack.study.enums.StudyCategory;
import com.studytrack.study.enums.Visibility;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudySettingDto {

    public record Request(
            @NotBlank(message = "스터디 제목은 필수입니다.")
            String title,

            @NotBlank(message = "설명을 입력해주세요.")
            String description,

            @Min(value = 2, message = "최소 인원은 2명 이상입니다.")
            @Max(value = 50, message = "최대 인원은 50 이하입니다.")
            int maxMembers,

            StudyCategory category,

            Visibility visibility
    ) {}

    public record Response(
            Long id,
            String title,
            String description,
            int maxMembers,
            StudyCategory category,
            Visibility visibility
    ) {}

}
