package com.studytrack.auth.dto;

public class LoginDto {

    public record LoginRequest(
            String userId,
            String password
    ) {}

    public record LoginResponse(
            String accessToken,
            String userId,
            String nickname
    ) {}
}
