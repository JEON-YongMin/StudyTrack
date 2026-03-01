package com.studytrack.study.controller;

import com.studytrack.study.dto.NoticeDto;
import com.studytrack.study.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study/{studyId}/notice")
public class NoticeRestController {

    private final NoticeService noticeService;

    @PostMapping
    public NoticeDto.Response create(@PathVariable Long studyId,
                                      @RequestBody @Valid NoticeDto.Request request) {

        return noticeService.create(studyId, request);

    }

    @PutMapping("/{noticeId}")
    public NoticeDto.Response update(@PathVariable Long studyId,
                                         @PathVariable Long noticeId,
                                         @Valid @RequestBody NoticeDto.Request req) {
        return noticeService.update(studyId, noticeId, req);
    }

    @DeleteMapping("/{noticeId}")
    public void delete(@PathVariable Long studyId, @PathVariable Long noticeId) {
        noticeService.delete(studyId, noticeId); // 서비스 레이어의 삭제 로직 호출
    }
}
