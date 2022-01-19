package inu.market.trade.domain;

import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.domain.Status;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        tradeRepository.deleteAll();
    }

    @Test
    @DisplayName("상품과 관련된 거래를 삭제한다.")
    void deleteAllByItem() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        Trade trade = Trade.createTrade(item, user);
        tradeRepository.save(trade);

        // when
        tradeRepository.deleteAllByItem(item);

        // then
        List<Trade> result = tradeRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }
}