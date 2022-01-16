package inu.market.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public static NoticeResponse simpleFrom(Notice notice){
        NoticeResponse noticeResponse = new NoticeResponse();
        noticeResponse.noticeId = notice.getId();
        noticeResponse.title = notice.getTitle();
        noticeResponse.updatedAt = notice.getUpdatedAt();
        return noticeResponse;
    }

    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(), notice.getUpdatedAt());
    }
}
