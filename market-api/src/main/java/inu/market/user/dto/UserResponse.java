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

    private String profileUrl;

    private Double score;

    private boolean notification;

    public static UserResponse from(User user) {
        UserResponse userResponse = new UserResponse(user.getId(), user.getInuId(), user.getNickName(), null,
                                                     user.getScore(), user.isNotification());
        if (user.getImage() != null) {
            userResponse.profileUrl = user.getImage().getImageUrl();
        }
        return userResponse;
    }

}
