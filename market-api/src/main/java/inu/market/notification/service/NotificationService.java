package inu.market.notification.service;

import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationQueryRepository;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationQueryRepository notificationQueryRepository;

    public List<NotificationResponse> findByUserId(Long userId, Long notificationId) {
        List<Notification> notifications = notificationQueryRepository.findByUserId(userId, notificationId);
        return notifications.stream()
                .map(notification -> NotificationResponse.from(notification))
                .collect(Collectors.toList());
    }
}
