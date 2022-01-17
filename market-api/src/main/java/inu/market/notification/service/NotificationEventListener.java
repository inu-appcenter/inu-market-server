package inu.market.notification.service;

import inu.market.client.FirebaseClient;
import inu.market.common.NotificationEvent;
import inu.market.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final FirebaseClient firebaseClient;

    @TransactionalEventListener
    public void handleNotification(NotificationEvent event) {
        Notification notification = (Notification) event.getSource();
        firebaseClient.send(notification.getUser().getPushToken(), "INOM", notification.getContent());
    }
}
