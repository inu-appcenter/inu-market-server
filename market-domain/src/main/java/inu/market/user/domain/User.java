package inu.market.user.domain;

import inu.market.common.BaseEntity;
import inu.market.common.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private int inuId;

    private String nickName;

    @Embedded
    private Image image;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double score;

    private boolean notification;

    private String pushToken;

    public static User createUser(int inuId, Role role) {
        User user = new User();
        user.inuId = inuId;
        user.role = role;
        user.score = 0.0;
        user.notification = true;
        return user;
    }

    public void changePushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void changeImage(Image image) {
        this.image = image;
    }

    public void changeNotification(Boolean notification) {
        this.notification = notification;
    }
}
