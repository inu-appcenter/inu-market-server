package inu.market.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @NotNull(message = "학번은 필수입니다.")
    @Positive(message = "학번은 양수이어야 합니다.")
    private Integer inuId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "푸쉬 토큰은 필수입니다.")
    private String pushToken;
}
