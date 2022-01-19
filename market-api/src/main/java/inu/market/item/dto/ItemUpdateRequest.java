package inu.market.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "설명은 필수입니다.")
    private String contents;

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 양수이어야 합니다.")
    private Integer price;

    @NotNull(message = "학과 ID는 필수입니다.")
    @Positive(message = "학과 ID는 양수이어야 합니다.")
    private Long majorId;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    @Positive(message = "카테고리 ID는 양수이어야 합니다.")
    private Long categoryId;

    @NotNull(message = "상품 이미지는 필수입니다.")
    private List<String> imageUrls;
}
