package inu.market.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String contents;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mainImageUrl;

    private int price;

    private int favoriteCount;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean favorite;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private MajorResponse major;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private CategoryResponse category;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserResponse seller;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> imageUrls;

    @QueryProjection
    public ItemResponse(Long itemId,String title, String mainImageUrl, int price,
                        int favoriteCount, String status, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.itemId = itemId;
        this.title = title;
        this.mainImageUrl = mainImageUrl;
        this.price = price;
        this.favoriteCount = favoriteCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ItemResponse from(Item item, List<ItemImage> itemImages, boolean favorite) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.itemId = item.getId();
        itemResponse.title = item.getTitle();
        itemResponse.contents = item.getContents();
        itemResponse.price = item.getPrice();
        itemResponse.favoriteCount = item.getFavoriteCount();
        itemResponse.status = item.getStatus().getStatus();
        itemResponse.favorite = favorite;
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

}
