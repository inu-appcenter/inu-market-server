package inu.market.favorite.domain;

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
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        favoriteRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 ID 와 상품 ID로 찜과 찜 상품을 조회한다.")
    void findWithItemByUserIdAndItemId() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, user);
        itemRepository.save(item);

        Favorite favorite = Favorite.createFavorite(user, item);
        favoriteRepository.save(favorite);
        // when
        Favorite result = favoriteRepository.findWithItemByUserIdAndItemId(user.getId(), item.getId()).get();

        // then
        assertThat(result).isEqualTo(favorite);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getItem()).isEqualTo(item);
    }

    @Test
    @DisplayName("회원 ID 와 상품 ID로 찜을 조회한다.")
    void findByUserIdAndItemId() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, user);
        itemRepository.save(item);

        Favorite favorite = Favorite.createFavorite(user, item);
        favoriteRepository.save(favorite);

        // when
        Favorite result = favoriteRepository.findByUserIdAndItemId(user.getId(), item.getId()).get();

        // then
        assertThat(result).isEqualTo(favorite);
    }

    @Test
    @DisplayName("상품의 좋아요를 모두 삭제한다.")
    void deleteAllByItem() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, user);
        itemRepository.save(item);

        Favorite favorite = Favorite.createFavorite(user, item);
        favoriteRepository.save(favorite);

        // when
        favoriteRepository.deleteAllByItem(item);

        // then
        List<Favorite> result = favoriteRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }
}