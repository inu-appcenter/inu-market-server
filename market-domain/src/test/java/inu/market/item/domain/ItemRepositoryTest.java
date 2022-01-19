package inu.market.item.domain;

import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("상품과 판매자를 같이 조회한다.")
    void findWithSellerById() {
        // given
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        // when
        Item result = itemRepository.findWithSellerById(item.getId()).get();

        // then
        assertThat(result).isEqualTo(item);
        assertThat(result.getSeller()).isEqualTo(seller);
    }
}