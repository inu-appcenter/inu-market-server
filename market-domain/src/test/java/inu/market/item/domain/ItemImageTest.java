package inu.market.item.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemImageTest {

    @Test
    @DisplayName("상품 이미지를 생성한다.")
    void createItemImage() {
        // given
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);
        String imageUrl = "이미지 URL";

        // when
        ItemImage result = ItemImage.createItemImage(imageUrl, item);

        // then
        assertThat(result.getImageUrl()).isEqualTo(imageUrl);
        assertThat(result.getItem()).isEqualTo(item);
    }
}