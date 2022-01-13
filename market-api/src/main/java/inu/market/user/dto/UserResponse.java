package inu.market.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long userId;

    private int inuId;

    private String nickName;

    private String profileUrl;

    private Double score;

    private boolean notification;

}
