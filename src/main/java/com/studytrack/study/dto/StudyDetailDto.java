package com.studytrack.study.dto;

import com.studytrack.study.enums.StudyCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public class StudyDetailDto {

    @Schema(description = "스터디 상세 응답")
    public record StudyDetailResponse(
            @Schema(example = "1") Long studyId,
            @Schema(example = "알고리즘 스터디") String title,
            @Schema(example = "매주 알고리즘 문제 풀이 및 코드 리뷰") String description,
            @Schema(example = "5") Integer maxMembers,
            @Schema(example = "CS") StudyCategory category,
            @Schema(example = "STUDY-AB12CD") String inviteCode,
            @Schema(example = "1") Integer memberCount) {}
}
