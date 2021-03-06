package inu.market.item.domain;

import inu.market.category.domain.Category;
import inu.market.common.BaseEntity;
import inu.market.major.domain.Major;
import inu.market.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String title;

    private String contents;

    private String mainImageUrl;

    private int price;

    private int favoriteCount;

    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    public static Item createItem(String title, String contents, String mainImageUrl,
                                  int price, Status status, User seller) {
        Item item = new Item();
        item.title = title;
        item.contents = contents;
        item.mainImageUrl = mainImageUrl;
        item.price = price;
        item.favoriteCount = 0;
        item.status = status;
        item.seller = seller;
        return item;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changeMajor(Major major) {
        this.major = major;
    }

    public void changeItemImages(List<String> imageUrls) {
        List<ItemImage> requestItemImages = imageUrls.stream()
                .map(imageUrl -> ItemImage.createItemImage(imageUrl, this))
                .collect(Collectors.toList());

        this.mainImageUrl = requestItemImages.size() != 0 ? requestItemImages.get(0).getImageUrl() : null;
        this.itemImages.clear();
        this.itemImages.addAll(requestItemImages);
    }

    public void changeTitleAndContentAndPrice(String title, String contents, int price) {
        this.title = title;
        this.contents = contents;
        this.price = price;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void increaseFavoriteCount() {
        this.favoriteCount++;
    }

    public void decreaseFavoriteCount() {
        this.favoriteCount--;
    }
}
