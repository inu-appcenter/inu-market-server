package inu.market.notice.service;

import inu.market.notice.domain.Notice;
import inu.market.notice.domain.NoticeRepository;
import inu.market.notice.dto.NoticeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
