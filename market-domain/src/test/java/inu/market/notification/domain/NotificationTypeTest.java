package inu.market.notification.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NotificationTypeTest {

    @Test
    @DisplayName("알림 타입을 문자열로 얻는다.")
    void getType() {
        // when
        String result = NotificationType.TRADE.getType();

        // then
        assertThat(result).isEqualTo("거래");
    }
}