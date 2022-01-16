package inu.market.notification.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.market.notification.domain.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Notification> findByUserId(Long userId, Long notificationId) {
        return queryFactory
                .selectFrom(notification)
                .where(notification.user.id.eq(userId),
                        notificationIdLt(notificationId))
                .orderBy(notification.id.desc())
                .fetch();
    }

    private BooleanExpression notificationIdLt(Long notificationId) {
        return notificationId != null ? notification.id.lt(notificationId) : null;
    }

}
