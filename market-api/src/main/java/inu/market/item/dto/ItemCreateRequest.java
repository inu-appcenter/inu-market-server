package inu.market.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {

    private String title;

    private String contents;

    private int price;

    private Long majorId;

    private Long categoryId;

    private List<String> imageUrls;
}
