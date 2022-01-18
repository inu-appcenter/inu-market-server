package inu.market.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateProfileRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;

    private String imageUrl;

}
