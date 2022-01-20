package inu.market.notification.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.domain.NotificationType;
import inu.market.notification.dto.NotificationResponse;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class NotificationQueryRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private NotificationQueryRepository notificationQueryRepository;

    @BeforeEach
    void setUp() {
        notificationQueryRepository = new NotificationQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    @DisplayName("회원의 알림 첫페이지를 조회한다.")
    void findByUserId() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);

        Notification notification = Notification.createNotification("내용", NotificationType.TRADE, 1L, user);
        notificationRepository.save(notification);

        // when
        List<NotificationResponse> result = notificationQueryRepository.findByUserId(user.getId(), null);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원의 알림 다음 페이지를 조회한다.")
    void findByUserIdLtNotificationId() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);

        Notification notification = Notification.createNotification("내용", NotificationType.TRADE, 1L, user);
        notificationRepository.save(notification);

        // when
        List<NotificationResponse> result = notificationQueryRepository.findByUserId(user.getId(), 10L);

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}