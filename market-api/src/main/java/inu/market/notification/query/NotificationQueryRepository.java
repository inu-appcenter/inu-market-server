package inu.market.notification.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.market.notification.dto.NotificationResponse;
import inu.market.notification.dto.QNotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.market.notification.domain.QNotification.notification;


@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<NotificationResponse> findByUserId(Long userId, Long notificationId) {
        return queryFactory
                .select(new QNotificationResponse(notification.id, notification.content,
                                                  notification.read, notification.notificationType,
                                                  notification.referenceId, notification.createdAt))
                .from(notification)
                .where(notification.user.id.eq(userId),
                        notificationIdLt(notificationId))
                .orderBy(notification.id.desc())
                .fetch();
    }

    private BooleanExpression notificationIdLt(Long notificationId) {
        return notificationId != null ? notification.id.lt(notificationId) : null;
    }

}
