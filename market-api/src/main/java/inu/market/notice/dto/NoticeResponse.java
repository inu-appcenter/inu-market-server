package inu.market.notice.dto;

import inu.market.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponse {

    private Long noticeId;

    private String title;

    private String content;

    private LocalDateTime updatedAt;

    public static NoticeResponse simpleFrom(Notice notice){
        NoticeResponse noticeResponse = new NoticeResponse();
        noticeResponse.noticeId = notice.getId();
        noticeResponse.title = notice.getTitle();
        noticeResponse.updatedAt = notice.getUpdatedAt();
        return noticeResponse;
    }

}
