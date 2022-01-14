package inu.market.category.dto;

import inu.market.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long categoryId;

    private String name;

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}
