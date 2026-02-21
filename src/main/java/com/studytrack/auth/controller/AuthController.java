package com.studytrack.auth.controller;

import com.studytrack.auth.dto.LoginDto;
import com.studytrack.auth.dto.SignupDto.SignupRequest;
import com.studytrack.auth.dto.SignupDto.SignupResponse;
import com.studytrack.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody @Valid SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public LoginDto.LoginResponse login(@RequestBody @Valid LoginDto.LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/status")
    public Map<String, Object> status(Authentication authentication) {
        return Map.of(
                "authenticated", authentication != null && authentication.isAuthenticated(),
                "userId", authentication != null ? authentication.getName() : null
        );
    }
}
