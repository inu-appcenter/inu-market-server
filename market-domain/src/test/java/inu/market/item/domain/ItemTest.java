package inu.market.item.domain;

import inu.market.category.domain.Category;
import inu.market.major.domain.Major;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    @DisplayName("상품을 생성한다.")
    void createItem() {
        // given
        User seller = User.createUser(201601757, Role.ROLE_USER);
        String title = "제목";
        String contents = "내용";
        String mainImageUrl = "이미지 URL";
        int price = 1000;
        Status status = Status.SALE;

        // when
        Item result = Item.createItem(title, contents, mainImageUrl, price, status, seller);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContents()).isEqualTo(contents);
        assertThat(result.getMainImageUrl()).isEqualTo(mainImageUrl);
        assertThat(result.getPrice()).isEqualTo(price);
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getSeller()).isEqualTo(seller);
    }

    @Test
    @DisplayName("카테고리를 변경한다.")
    void changeCategory() {
        // given
        Category category = Category.createCategory("도서", "이미지 URL");
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);

        // when
        item.changeCategory(category);

        // then
        assertThat(item.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("학과를 변경한다.")
    void changeMajor() {
        // given
        Major major = Major.createMajor("정보통신공학과", null);
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);

        // when
        item.changeMajor(major);

        // then
        assertThat(item.getMajor()).isEqualTo(major);
    }

    @Test
    @DisplayName("이미지를 변경한다.")
    void changeItemImages() {
        // given
        Item item = Item.createItem("제목", "내용", null, 1000, Status.SALE, null);
        List<String> imageUrls = Arrays.asList("이미지 URL", "이미지 URL");

        // when
        item.changeItemImages(imageUrls);

        // then
        assertThat(item.getMainImageUrl()).isEqualTo(imageUrls.get(0));
        assertThat(item.getItemImages().size()).isEqualTo(imageUrls.size());
    }

    @Test
    @DisplayName("이미지를 제거한다.")
    void changeNoItemImages() {
        // given
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);
        List<String> imageUrls = Arrays.asList();

        // when
        item.changeItemImages(imageUrls);

        // then
        assertThat(item.getMainImageUrl()).isNull();
        assertThat(item.getItemImages().size()).isEqualTo(imageUrls.size());
    }

    @Test
    @DisplayName("제목/설명/가격을 변경한다.")
    void changeTitleAndContentAndPrice() {
        // given
        Item item = Item.createItem(null, null, "이미지 URL", 0, Status.SALE, null);

        // when
        item.changeTitleAndContentAndPrice("제목", "내용", 1000);

        // then
        assertThat(item.getTitle()).isEqualTo("제목");
        assertThat(item.getContents()).isEqualTo("내용");
        assertThat(item.getPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("상태를 변경한다.")
    void changeStatus() {
        // given
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);

        // when
        item.changeStatus(Status.COMPLETED);

        // then
        assertThat(item.getStatus()).isEqualTo(Status.COMPLETED);
    }

    @Test
    @DisplayName("찜 회수를 증가시킨다.")
    void increaseFavoriteCount() {
        // given
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);

        // when
        item.increaseFavoriteCount();

        // then
        assertThat(item.getFavoriteCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("찜 회수를 감소시킨다.")
    void decreaseFavoriteCount() {
        // given
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);

        // when
        item.decreaseFavoriteCount();

        // then
        assertThat(item.getFavoriteCount()).isEqualTo(-1);
    }
}