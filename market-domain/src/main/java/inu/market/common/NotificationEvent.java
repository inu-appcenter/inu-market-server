package inu.market.common;

import inu.market.notification.domain.Notification;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    public NotificationEvent(Object source) {
        super(source);
    }

    public static NotificationEvent create(Notification notification){
        return new NotificationEvent(notification);
    }
}
