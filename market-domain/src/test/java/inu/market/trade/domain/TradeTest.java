package inu.market.trade.domain;

import inu.market.item.domain.Item;
import inu.market.item.domain.Status;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TradeTest {

    @Test
    @DisplayName("거래를 생성한다.")
    void createTrade() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);

        // when
        Trade result = Trade.createTrade(item, user);

        // then
        assertThat(result.getItem()).isEqualTo(item);
        assertThat(result.getBuyer()).isEqualTo(user);
        assertThat(result.getBuyer().getScore()).isEqualTo(10.0);
        assertThat(result.getItem().getSeller().getScore()).isEqualTo(10.0);
    }
}