package inu.market.notification;

import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationType;
import inu.market.notification.dto.NotificationResponse;
import inu.market.user.UserFixture;

import java.time.LocalDateTime;

public class NotificationFixture {

    public static final String TEST_NOTIFICATION_CONTENT = "내용";
    public static final boolean TEST_NOTIFICATION_READ = false;
    public static final NotificationType TEST_NOTIFICATION_TYPE = NotificationType.TRADE;
    public static final Long TEST_NOTIFICATION_REFERENCE_ID = 1L;

    public static final Notification TEST_NOTIFICATION
            = new Notification(1L, TEST_NOTIFICATION_CONTENT, TEST_NOTIFICATION_READ,
            TEST_NOTIFICATION_TYPE, TEST_NOTIFICATION_REFERENCE_ID,
            LocalDateTime.now(), LocalDateTime.now(), UserFixture.TEST_USER);

    public static final NotificationResponse TEST_NOTIFICATION_RESPONSE
            = new NotificationResponse(TEST_NOTIFICATION.getId(), TEST_NOTIFICATION.getContent(),
            TEST_NOTIFICATION.isRead(), TEST_NOTIFICATION.getNotificationType().toString(),
            TEST_NOTIFICATION.getReferenceId(), TEST_NOTIFICATION.getCreatedAt());
}
