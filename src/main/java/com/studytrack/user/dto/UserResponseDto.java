package com.studytrack.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private String userId;
    private String nickname;
}