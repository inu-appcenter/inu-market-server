package inu.market.notification.service;

import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.dto.NotificationResponse;
import inu.market.notification.query.NotificationQueryRepository;
import inu.market.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.notification.NotificationFixture.TEST_NOTIFICATION;
import static inu.market.notification.NotificationFixture.TEST_NOTIFICATION_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationQueryRepository notificationQueryRepository;

    @Test
    @DisplayName("회원의 알림을 조회한다.")
    void findByUserId() {
        // given
        given(notificationQueryRepository.findByUserId(any(), any()))
                .willReturn(Arrays.asList(TEST_NOTIFICATION_RESPONSE));

        // when
        List<NotificationResponse> result = notificationService.findByUserId(any(), any());

        // then
        assertThat(result.size()).isEqualTo(1);
        then(notificationQueryRepository).should(times(1)).findByUserId(any(), any());
    }

    @Test
    @DisplayName("알림을 읽음 처리한다.")
    void updateRead() {
        // given
        given(notificationRepository.findById(any()))
                .willReturn(Optional.of(TEST_NOTIFICATION));

        // when
        notificationService.updateRead(UserFixture.TEST_USER.getId(), TEST_NOTIFICATION.getId());

        // then
        then(notificationRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("알림의 회원이 아닌 회원이 읽음 처리하면 예외가 발생한다.")
    void updateReadNotOwner() {
        // given
        given(notificationRepository.findById(any()))
                .willReturn(Optional.of(TEST_NOTIFICATION));

        // when
        assertThrows(AccessDeniedException.class, () -> notificationService.updateRead(UserFixture.TEST_USER1.getId(), TEST_NOTIFICATION.getId()));

        // then
        then(notificationRepository).should(times(1)).findById(any());
    }
}