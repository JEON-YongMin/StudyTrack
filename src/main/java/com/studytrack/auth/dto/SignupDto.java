package com.studytrack.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class SignupDto {

    public record SignupRequest(
            @NotBlank String userId,
            @NotBlank String password,
            String nickname
    ) {}

    public record SignupResponse(
            Long id,
            String userId,
            String nickname
    ) {}
}
