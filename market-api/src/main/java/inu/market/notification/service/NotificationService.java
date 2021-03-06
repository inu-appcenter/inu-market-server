package inu.market.notification.service;

import inu.market.common.NotFoundException;
import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.dto.NotificationResponse;
import inu.market.notification.query.NotificationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static inu.market.common.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> findByUserId(Long userId, Long notificationId) {
        return notificationQueryRepository.findByUserId(userId, notificationId);
    }

    @Transactional
    public void updateRead(Long userId, Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(NOTIFICATION_NOT_FOUND));

        if (!findNotification.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("");
        }

        findNotification.changeRead();
    }
}
