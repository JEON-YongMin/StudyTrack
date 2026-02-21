package com.studytrack.auth.service;

import com.studytrack.auth.dto.LoginDto;
import com.studytrack.auth.dto.SignupDto.SignupRequest;
import com.studytrack.auth.dto.SignupDto.SignupResponse;
import com.studytrack.auth.jwt.JwtProvider;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.studytrack.auth.exception.DuplicateUserException;
import com.studytrack.auth.exception.AuthFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public SignupResponse signup(SignupRequest request) {
        String userId = request.userId().trim();

        // 1) userId 중복 체크
        if (userRepository.existsByUserId(userId)) {
            throw new DuplicateUserException("이미 존재하는 userId입니다.");
        }

        // 2) nickname 없으면 userId로
        String nickname = (request.nickname() == null || request.nickname().isBlank())
                ? userId
                : request.nickname().trim();

        // 3) 비밀번호 해시(BCrypt)
        String encodedPassword = passwordEncoder.encode(request.password());

        // 4) 저장
        User saved = userRepository.save(
                User.builder()
                        .userId(userId)
                        .password(encodedPassword)
                        .nickname(nickname)
                        .build()
        );

        // 5) 응답
        return new SignupResponse(saved.getId(), saved.getUserId(), saved.getNickname());
    }

    @Transactional(readOnly = true)
    public LoginDto.LoginResponse login(LoginDto.LoginRequest request) {

        User user = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new AuthFailedException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthFailedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtProvider.createAccessToken(user.getUserId());

        return new LoginDto.LoginResponse(token, user.getUserId(), user.getNickname());
    }
}
