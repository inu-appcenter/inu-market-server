package inu.market.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;

    private String content;

    private boolean read;

    private String notificationType;

    private Long referenceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @QueryProjection
    public NotificationResponse(Long notificationId, String content, boolean read, String notificationType, Long referenceId, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.content = content;
        this.read = read;
        this.notificationType = notificationType;
        this.referenceId = referenceId;
        this.createdAt = createdAt;
    }

}
