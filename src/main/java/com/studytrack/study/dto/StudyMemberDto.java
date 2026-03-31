package com.studytrack.study.dto;

import com.studytrack.study.enums.StudyRole;
import com.studytrack.user.entity.User;

public record StudyMemberDto(
        User user,
        String nickname,
        StudyRole role
) {}
