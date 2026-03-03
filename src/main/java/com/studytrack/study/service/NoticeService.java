package com.studytrack.study.service;

import com.studytrack.study.dto.NoticeDto;
import com.studytrack.study.entity.Assignment;
import com.studytrack.study.entity.Notice;
import com.studytrack.study.entity.Study;
import com.studytrack.study.repository.NoticeRepository;
import com.studytrack.study.repository.StudyRepository;
import com.studytrack.user.entity.User;
import com.studytrack.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public NoticeDto.Response create(Long studyId, NoticeDto.Request req) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 없습니다. studyId=" + studyId));

        String userId = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자를 찾을 수 없습니다."));


        Notice notice = new Notice(study, user.getUserId(), req.title(), req.content());
        Notice saved = noticeRepository.save(notice);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NoticeDto.Response get(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다. id=" + noticeId));
        return toResponse(notice);
    }

    public NoticeDto.Response update(Long studyId, Long noticeId, NoticeDto.Request req) {
        Notice notice = noticeRepository.findByIdAndStudyId(studyId, noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다. id=" + noticeId));

        String userId = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        // ✅ 작성자만 수정 가능
        if (!notice.getAuthorId().equals(userId)) {
            throw new SecurityException("작성자만 수정할 수 있습니다.");
        }

        notice.update(req.title(), req.content());
        return toResponse(notice);
    }

    @Transactional(readOnly = true)
    public boolean canEdit(Long noticeId, Long userId) {
        if (userId == null) return false;
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다. id=" + noticeId));
        return notice.getAuthorId().equals(userId);
    }

    private NoticeDto.Response toResponse(Notice n) {
        return new NoticeDto.Response(
                n.getId(),
                n.getStudy().getId(),
                n.getTitle(),
                n.getContent(),
                n.getAuthorId(),
                n.getCreatedAt()
        );
    }

    @Transactional
    public void delete(Long studyId, Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. id=" + noticeId));

        if (!notice.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("해당 스터디의 과제가 아닙니다. studyId=" + studyId);
        }

        noticeRepository.delete(notice);
    }
}
