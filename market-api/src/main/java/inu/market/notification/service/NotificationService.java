package inu.market.notification.service;

import inu.market.common.NotFoundException;
import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationQueryRepository;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> findByUserId(Long userId, Long notificationId) {
        List<Notification> notifications = notificationQueryRepository.findByUserId(userId, notificationId);
        return notifications.stream()
                .map(notification -> NotificationResponse.from(notification))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRead(Long userId, Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(notificationId + "는 존재하지 않는 알림 ID 입니다."));

        if (!findNotification.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        findNotification.changeRead();
    }
}
