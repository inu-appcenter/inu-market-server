package inu.market.notification.domain;

import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    @DisplayName("알림을 생성한다.")
    void createNotification() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);
        String content = "내용";
        NotificationType notificationType = NotificationType.TRADE;
        Long referenceId = 1L;

        // when
        Notification result = Notification.createNotification(content, notificationType, referenceId, user);

        // then
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getNotificationType()).isEqualTo(notificationType);
        assertThat(result.getReferenceId()).isEqualTo(referenceId);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.isRead()).isFalse();
    }

    @Test
    @DisplayName("알림생성 이벤트를 발행한다.")
    void create() {
        // given
        Notification notification = Notification.createNotification("내용", NotificationType.TRADE, 1L, null);

        // when
        Notification result = notification.create();

        // then
        assertThat(result).isEqualTo(notification);
    }

    @Test
    @DisplayName("읽음으로 변경한다.")
    void changeRead() {
        // given
        Notification notification = Notification.createNotification("내용", NotificationType.TRADE, 1L, null);

        // when
        notification.changeRead();

        // then
        assertThat(notification.isRead()).isTrue();
    }
}