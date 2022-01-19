package inu.market.item.domain;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @Test
    @DisplayName("상품과 상품 이미지를 같이 조회한다.")
    void findWithItemImagesById(){
        // given
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, null);
        item.changeItemImages(Arrays.asList("이미지 URL"));
        itemRepository.save(item);

        // when
        Item result = itemRepository.findWithItemImagesById(item.getId()).get();

        // then
        assertThat(result).isEqualTo(item);
        assertThat(result.getItemImages().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품과 판매자/상품 이미지/카테고리/학과를 같이 조회한다.")
    void findWithSellerAndItemImagesAndCategoryAndMajorById(){
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
        Item result = itemRepository.findWithSellerAndItemImagesAndCategoryAndMajorById(item.getId()).get();

        // then
        assertThat(result).isEqualTo(item);
        assertThat(result.getSeller()).isEqualTo(seller);
        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getMajor()).isEqualTo(major);
        assertThat(result.getItemImages().size()).isEqualTo(1);
    }
}