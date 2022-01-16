package inu.market.notice.service;

import inu.market.notice.domain.Notice;
import inu.market.notice.domain.NoticeRepository;
import inu.market.notice.dto.NoticeCreateRequest;
import inu.market.notice.dto.NoticeResponse;
import inu.market.notice.dto.NoticeUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Long create(NoticeCreateRequest request) {
        Notice notice = Notice.createNotice(request.getTitle(), request.getContent());
        noticeRepository.save(notice);
        return notice.getId();
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateRequest request) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항입니다."));

        findNotice.changeTitleAndContent(request.getTitle(), request.getContent());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항입니다."));

        noticeRepository.delete(findNotice);
    }

    public List<NoticeResponse> findAll() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(notice -> NoticeResponse.simpleFrom(notice))
                .collect(Collectors.toList());
    }

    public NoticeResponse findById(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공지사항입니다."));
        return NoticeResponse.from(findNotice);
    }
}
