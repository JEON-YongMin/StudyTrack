package com.studytrack.user.controller;

import com.studytrack.user.dto.UserResponseDto;
import com.studytrack.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyPageInfo(@RequestParam String userId) {

        UserResponseDto response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update_nickname")
    public ResponseEntity<String> updateNickname(
            @RequestParam String userId,
            @RequestParam String newNickname) {

        userService.updateNickname(userId, newNickname);
        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }
}