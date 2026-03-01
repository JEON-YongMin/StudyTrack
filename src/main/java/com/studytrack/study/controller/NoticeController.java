package com.studytrack.study.controller;

import com.studytrack.study.dto.NoticeDto;
import com.studytrack.study.entity.Study;
import com.studytrack.study.entity.StudyMember;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.study.service.NoticeService;
import com.studytrack.study.service.StudyMemberService;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}")
public class NoticeController {

    private final NoticeService noticeService;
    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;
    private final UserRepository userRepository;

    // ✅ create/view/edit 진입을 하나로
    @GetMapping({"/notice", "/notice/{noticeId}"})
    public String noticePage(
            @PathVariable Long studyId,
            @PathVariable(required = false) Long noticeId,
            @RequestParam(defaultValue = "view") String mode,
            Model model
    ) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = String.valueOf(principal);


        StudyMember ownerName = studyMemberService.getOwnerMember(studyId);

        model.addAttribute("study", study);
        model.addAttribute("ownerName", ownerName.getNickname());
        model.addAttribute("mode", mode);

        if ("create".equals(mode)) {
            // create: 빈 폼
            model.addAttribute("notice", null);
            return "study/notice";
        }else {
            // view/edit: noticeId 필요
            if (noticeId == null) throw new IllegalArgumentException("noticeId가 필요합니다.");
        }


        NoticeDto.Response notice = noticeService.get(noticeId);
        User author = userRepository.findByUserId(notice.authorId()).orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        model.addAttribute("notice", notice);
        model.addAttribute("author", author);

        return "study/notice";
    }

}
