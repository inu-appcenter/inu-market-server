package inu.market.notification.domain;

import inu.market.common.NotificationEvent;
import inu.market.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Notification extends AbstractAggregateRoot<Notification> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content;

    private boolean read;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long referenceId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Notification createNotification(String content, NotificationType notificationType, Long referenceId, User user) {
        Notification notification = new Notification();
        notification.content = content;
        notification.notificationType = notificationType;
        notification.read = false;
        notification.referenceId = referenceId;
        notification.user = user;
        return notification;
    }

    public Notification create(){
        this.registerEvent(NotificationEvent.create(this));
        return this;
    }


    public void changeRead() {
        this.read = true;
    }
}
