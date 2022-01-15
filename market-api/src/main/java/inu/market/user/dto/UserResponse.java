package inu.market.user.dto;

import inu.market.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;

    private int inuId;

    private String nickName;

    private String imageUrl;

    private Double score;

    private boolean notification;

    private String pushToken;

    public static UserResponse from(User user) {
        UserResponse userResponse = new UserResponse(user.getId(), user.getInuId(), user.getNickName(), user.getImageUrl(),
                                                     user.getScore(), user.isNotification(), user.getPushToken());
        return userResponse;
    }

}
