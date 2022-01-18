package inu.market.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    @DisplayName("카테고리를 생성한다.")
    void createCategory() {
        // given
        String name = "전자기기";
        String iconUrl = "아이콘 URL";

        // when
        Category category = Category.createCategory(name, iconUrl);

        // then
        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getIconUrl()).isEqualTo(iconUrl);
    }

    @Test
    @DisplayName("카테고리의 이름과 아이콘 URL 을 변경한다.")
    void changeNameAndIconUrl() {
        // given
        Category category = Category.createCategory("전자기기", "아이콘 URL");

        // when
        category.changeNameAndIconUrl("의류", "변경할 아이콘 URL");

        // then
        assertThat(category.getName()).isEqualTo("의류");
        assertThat(category.getIconUrl()).isEqualTo("변경할 아이콘 URL");
    }
}