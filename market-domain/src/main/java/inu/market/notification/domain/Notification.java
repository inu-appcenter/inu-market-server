package inu.market.notification.domain;

import com.sun.xml.bind.v2.schemagen.xmlschema.NoFixedFacet;
import inu.market.common.BaseEntity;
import inu.market.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content;

    private boolean read;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long referenceId;

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


    public void changeRead() {
        this.read = true;
    }
}
