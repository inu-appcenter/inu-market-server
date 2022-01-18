package inu.market.notice;

import inu.market.notice.domain.Notice;
import inu.market.notice.dto.NoticeCreateRequest;
import inu.market.notice.dto.NoticeResponse;
import inu.market.notice.dto.NoticeUpdateRequest;

import java.time.LocalDateTime;

public class NoticeFixture {

    public static final String TEST_TITLE = "공지사항";
    public static final String TEST_CONTENT = "공지사항";

    public static final Notice TEST_NOTICE = new Notice(1L, TEST_TITLE, TEST_CONTENT);

    public static final NoticeCreateRequest TEST_NOTICE_CREATE_REQUEST = new NoticeCreateRequest(TEST_TITLE, TEST_CONTENT);

    public static final NoticeUpdateRequest TEST_NOTICE_UPDATE_REQUEST = new NoticeUpdateRequest(TEST_TITLE, TEST_CONTENT);

    public static final NoticeResponse TEST_NOTICE_SIMPLE_RESPONSE
            = new NoticeResponse(TEST_NOTICE.getId(), TEST_NOTICE.getTitle(), null, LocalDateTime.now());

    public static final NoticeResponse TEST_NOTICE_RESPONSE
            = new NoticeResponse(TEST_NOTICE.getId(), TEST_NOTICE.getTitle(), TEST_CONTENT, LocalDateTime.now());
}
