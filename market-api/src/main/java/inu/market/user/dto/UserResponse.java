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

    private String nickName;

    private String imageUrl;

    private Double score;

    private String pushToken;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getNickName(), user.getImageUrl(),
                                                     user.getScore(), user.getPushToken());
    }

}
