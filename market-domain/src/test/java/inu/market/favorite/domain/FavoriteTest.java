package inu.market.favorite.domain;

import inu.market.item.domain.Item;
import inu.market.item.domain.Status;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @Test
    @DisplayName("찜을 생성한다.")
    void createFavorite() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);

        // when
        Favorite result = Favorite.createFavorite(user, item);

        // then
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getItem()).isEqualTo(item);
    }

}