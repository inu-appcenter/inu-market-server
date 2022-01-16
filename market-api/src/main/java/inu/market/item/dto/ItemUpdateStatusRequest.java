package inu.market.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateStatusRequest {

    @NotBlank(message = "상태는 필수입니다.")
    private String status;

}
