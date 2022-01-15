package inu.market.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchRequest {

    private Long categoryId;

    private Long majorId;

    private String searchWord;

    private Integer size;

    private Long itemId;

}
