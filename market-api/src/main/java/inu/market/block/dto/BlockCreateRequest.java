package inu.market.block.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockCreateRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    @Positive(message = "회원 ID는 양수이어야 합니다.")
    private Long userId;
}
