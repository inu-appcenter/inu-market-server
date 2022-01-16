package inu.market.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import inu.market.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;

    private String content;

    private boolean read;

    private String notificationType;

    private Long referenceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification.getId(),
                                        notification.getContent(),
                                        notification.isRead(),
                                        notification.getNotificationType().getType(),
                                        notification.getReferenceId(),
                                        notification.getCreatedAt());
    }
}
