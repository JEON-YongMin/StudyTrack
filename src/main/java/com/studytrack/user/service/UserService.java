package com.studytrack.user.service;

import com.studytrack.user.entity.User;
import com.studytrack.user.dto.UserResponseDto;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public void updateNickname(String userId, String newNickname) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateNickname(newNickname);

    }
}