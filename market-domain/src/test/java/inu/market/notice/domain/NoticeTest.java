package inu.market.notice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {

    @Test
    @DisplayName("공지사항을 생성한다.")
    void createNotice() {
        // given
        String title = "공지사항";
        String content = "공지사항";

        // when
        Notice result = Notice.createNotice(title, content);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("제목과 내용을 변경한다.")
    void changeTitleAndContent() {
        // given
        Notice notice = Notice.createNotice(null,null);

        // when
        notice.changeTitleAndContent("공지사항","공지사항");

        // then
        assertThat(notice.getTitle()).isEqualTo("공지사항");
        assertThat(notice.getContent()).isEqualTo("공지사항");
    }
}