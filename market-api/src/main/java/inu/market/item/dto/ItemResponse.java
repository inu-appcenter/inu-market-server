package inu.market.item.dto;

import inu.market.category.dto.CategoryResponse;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemImage;
import inu.market.major.dto.MajorResponse;
import inu.market.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {

    private Long itemId;

    private String title;

    private String contents;

    private String mainImageUrl;

    private int price;

    private int favoriteCount;

    private String status;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private MajorResponse major;

    private CategoryResponse category;

    private UserResponse seller;

    private List<String> imageUrls;

    public static ItemResponse from(Item item, List<ItemImage> itemImages) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.itemId = item.getId();
        itemResponse.title = item.getTitle();
        itemResponse.contents = item.getContents();
        itemResponse.mainImageUrl = item.getMainImageUrl();
        itemResponse.price = item.getPrice();
        itemResponse.favoriteCount = item.getFavoriteCount();
        itemResponse.status = item.getStatus().name();
        itemResponse.active = item.isActive();
        itemResponse.createdAt = item.getCreatedAt();
        itemResponse.updatedAt = item.getUpdatedAt();
        itemResponse.major = MajorResponse.from(item.getMajor());
        itemResponse.category = CategoryResponse.from(item.getCategory());
        itemResponse.seller = UserResponse.from(item.getSeller());
        itemResponse.imageUrls = itemImages.stream()
                .map(itemImage -> itemImage.getImageUrl())
                .collect(Collectors.toList());
        return itemResponse;
    }

    public static ItemResponse from(Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.itemId = item.getId();
        itemResponse.title = item.getTitle();
        itemResponse.mainImageUrl = item.getMainImageUrl();
        itemResponse.price = item.getPrice();
        itemResponse.favoriteCount = item.getFavoriteCount();
        itemResponse.status = item.getStatus().name();
        itemResponse.active = item.isActive();
        itemResponse.createdAt = item.getCreatedAt();
        itemResponse.updatedAt = item.getUpdatedAt();
        return itemResponse;
    }
}
