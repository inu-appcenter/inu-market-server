package inu.market.item.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.domain.Status;
import inu.market.item.dto.ItemResponse;
import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.trade.domain.Trade;
import inu.market.trade.domain.TradeRepository;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ItemQueryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private EntityManager em;

    private ItemQueryRepository itemQueryRepository;

    @BeforeEach
    void setUp() {
        itemQueryRepository = new ItemQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    @DisplayName("구매자의 상품을 조회한다.")
    void findByTradeBuyerId() {
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
        List<ItemResponse> result = itemQueryRepository.findByTradeBuyerId(user.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("판매자의 상품을 조회한다.")
    void findBySellerId() {
        // given
        User seller = User.createUser(201601757, Role.ROLE_USER);
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        userRepository.save(seller);
        itemRepository.save(item);

        // when
        List<ItemResponse> result = itemQueryRepository.findBySellerId(seller.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 조건에 맞는 상품을 검색한다.")
    void findBySearchCondition() {
        // given
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(seller);

        Major major = Major.createMajor("정보기술대학", null);
        majorRepository.save(major);

        Category category = Category.createCategory("전자기기", "아이콘 URL");
        categoryRepository.save(category);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        item.changeCategory(category);
        item.changeMajor(major);
        item.changeItemImages(Arrays.asList("이미지 URL"));
        itemRepository.save(item);

        // when
        List<ItemResponse> result = itemQueryRepository.findBySearchCondition(seller.getId(), 10L, category.getId(), major.getId(), "제목", 10);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 조건에 맞지 않는 상품을 검색한다.")
    void findByNotMatchSearchCondition() {
        // given
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        // when
        List<ItemResponse> result = itemQueryRepository.findBySearchCondition(seller.getId(), null, null, null, null, 10);

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}