package inu.market.notification.dto;

import inu.market.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;

    private String content;

    private boolean read;

    private String notificationType;

    private Long referenceId;

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification.getId(),
                                        notification.getContent(),
                                        notification.isRead(),
                                        notification.getNotificationType().getType(),
                                        notification.getReferenceId());
    }
}
