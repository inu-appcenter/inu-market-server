package inu.market.notice.service;

import inu.market.common.NotFoundException;
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

import static inu.market.common.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Long create(NoticeCreateRequest request) {
        Notice notice = Notice.createNotice(request.getTitle(), request.getContent());
        return noticeRepository.save(notice).getId();
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateRequest request) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException(NOTICE_NOT_FOUND));

        findNotice.changeTitleAndContent(request.getTitle(), request.getContent());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException(NOTICE_NOT_FOUND));

        noticeRepository.delete(findNotice);
    }

    public List<NoticeResponse> findAll() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(NoticeResponse::simpleFrom)
                .collect(Collectors.toList());
    }

    public NoticeResponse findById(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException(NOTICE_NOT_FOUND));
        return NoticeResponse.from(findNotice);
    }
}
