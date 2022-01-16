package inu.market.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchRequest {

    private Long categoryId;

    private Long majorId;

    private String searchWord;

    @NotNull(message = "사이즈는 필수입니다.")
    @Positive(message = "사이즈는 양수이어야 합니다.")
    private Integer size;

    private Long itemId;

}
