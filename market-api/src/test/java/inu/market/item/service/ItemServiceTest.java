package inu.market.item.service;

import inu.market.category.domain.CategoryRepository;
import inu.market.chatroom.domain.ChatRoomRepository;
import inu.market.client.AwsClient;
import inu.market.common.NotFoundException;
import inu.market.favorite.FavoriteFixture;
import inu.market.favorite.domain.FavoriteRepository;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.item.query.ItemQueryRepository;
import inu.market.major.domain.MajorRepository;
import inu.market.trade.domain.TradeRepository;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.CommonFixture.TEST_IMAGE_FILE;
import static inu.market.CommonFixture.TEST_IMAGE_URL;
import static inu.market.category.CategoryFixture.TEST_CATEGORY;
import static inu.market.item.ItemFixture.*;
import static inu.market.major.MajorFixture.TEST_MAJOR;
import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MajorRepository majorRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ItemQueryRepository itemQueryRepository;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private AwsClient awsClient;

    @Test
    @DisplayName("상품 이미지 ")
    void uploadImages() {
        // given
        given(awsClient.upload(any()))
                .willReturn(TEST_IMAGE_URL);
        // when
        List<String> result = itemService.uploadImages(Arrays.asList(TEST_IMAGE_FILE));

        // then
        assertThat(result.size()).isEqualTo(1);
        then(awsClient).should(times(1)).upload(any());
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        given(itemRepository.save(any()))
                .willReturn(TEST_ITEM);
        // when
        Long result = itemService.create(TEST_USER.getId(), TEST_ITEM_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_ITEM.getId());
        then(userRepository).should(times(1)).findById(any());
        then(categoryRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findById(any());
        then(itemRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("상품을 생성할 때 회원이 존재하지 않으면 예외가 발생한다.")
    void createCategoryNotFound() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.create(TEST_USER.getId(), TEST_ITEM_REQUEST));

        // then
        then(userRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품을 생성할 때 카테고리가 존재하지 않으면 예외가 발생한다.")
    void createItemNotFound() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.create(TEST_USER.getId(), TEST_ITEM_REQUEST));

        // then
        then(userRepository).should(times(1)).findById(any());
        then(categoryRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품을 생성할 때 전공이 존재하지 않으면 예외가 발생한다.")
    void createMajorNotFound() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        given(majorRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.create(TEST_USER.getId(), TEST_ITEM_REQUEST));

        // then
        then(userRepository).should(times(1)).findById(any());
        then(categoryRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품을 수정한다.")
    void update() {
        // given
        given(itemRepository.findWithItemImagesById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        // when
        itemService.update(TEST_USER.getId(), TEST_ITEM.getId(), TEST_ITEM_REQUEST);

        // then
        then(itemRepository).should(times(1)).findWithItemImagesById(any());
        then(categoryRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품의 판매자가 아닌 회원이 상품을 수정하면 예외가 발생한다.")
    void updateNotOwner() {
        // given
        given(itemRepository.findWithItemImagesById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        // when
        assertThrows(AccessDeniedException.class, () -> itemService.update(TEST_USER1.getId(), TEST_ITEM.getId(), TEST_ITEM_REQUEST));

        // then
        then(itemRepository).should(times(1)).findWithItemImagesById(any());
    }

    @Test
    @DisplayName("상품을 수정할 때 상품이 존재하지 않으면 예외가 발생한다.")
    void updateItemNotFound() {
        // given
        given(itemRepository.findWithItemImagesById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.update(TEST_USER.getId(), TEST_ITEM.getId(), TEST_ITEM_REQUEST));

        // then
        then(itemRepository).should(times(1)).findWithItemImagesById(any());
    }

    @Test
    @DisplayName("상품을 수정할 때 카테고리가 존재하지 않으면 예외가 발생한다.")
    void updateCategoryNotFound() {
        // given
        given(itemRepository.findWithItemImagesById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.update(TEST_USER.getId(), TEST_ITEM.getId(), TEST_ITEM_REQUEST));

        // then
        then(itemRepository).should(times(1)).findWithItemImagesById(any());
        then(categoryRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품을 수정할 때 카테고리가 존재하지 않으면 예외가 발생한다.")
    void updateMajorNotFound() {
        // given
        given(itemRepository.findWithItemImagesById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        given(majorRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.update(TEST_USER.getId(), TEST_ITEM.getId(), TEST_ITEM_REQUEST));

        // then
        then(itemRepository).should(times(1)).findWithItemImagesById(any());
        then(categoryRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품 상태를 수정한다.")
    void updateStatus() {
        // given
        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        // when
        itemService.updateStatus(TEST_USER.getId(), TEST_ITEM.getId(), TEST_ITEM_UPDATE_STATUS_REQUEST);

        // then
        then(itemRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품 판매자가 아닌 회원이 상품 상태를 수정하면 예외가 발생한다.")
    void updateStatusNotOwner() {
        // given
        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        // when
        assertThrows(AccessDeniedException.class, () -> itemService.updateStatus(TEST_USER1.getId(), TEST_ITEM.getId(), TEST_ITEM_UPDATE_STATUS_REQUEST));

        // then
        then(itemRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품 상태를 수정할 때 상품이 존재하지 않으면 예외가 발생한다.")
    void updateStatusNotFound() {
        // given
        given(itemRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.updateStatus(TEST_USER.getId(), TEST_ITEM.getId(), TEST_ITEM_UPDATE_STATUS_REQUEST));

        // then
        then(itemRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void delete() {
        // given
        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        willDoNothing()
                .given(tradeRepository)
                .deleteAllByItem(any());

        willDoNothing()
                .given(favoriteRepository)
                .deleteAllByItem(any());

        willDoNothing()
                .given(chatRoomRepository)
                .deleteAllByItem(any());

        willDoNothing()
                .given(itemRepository)
                .delete(any());
        // when
        itemService.delete(TEST_USER.getId(), TEST_ITEM.getId());

        // then
        then(itemRepository).should(times(1)).findById(any());
        then(tradeRepository).should(times(1)).deleteAllByItem(any());
        then(favoriteRepository).should(times(1)).deleteAllByItem(any());
        then(favoriteRepository).should(times(1)).deleteAllByItem(any());
        then(chatRoomRepository).should(times(1)).deleteAllByItem(any());
    }

    @Test
    @DisplayName("상품을 삭제할 때 상품이 존재하지 않으면 예외가 발생한다.")
    void deleteNotFound() {
        // given
        given(itemRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.delete(TEST_USER.getId(), TEST_ITEM.getId()));

        // then
        then(itemRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품 판매자가 아닌 회원이 상품을 삭제하면 예외가 발생한다.")
    void deleteNotOwner() {
        // given
        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        // when
        assertThrows(AccessDeniedException.class, () -> itemService.delete(TEST_USER1.getId(), TEST_ITEM.getId()));

        // then
        then(itemRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품을 상세 조회한다.")
    void findById() {
        // given
        given(itemRepository.findWithSellerAndItemImagesAndCategoryAndMajorById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(favoriteRepository.findByUserIdAndItemId(any(), any()))
                .willReturn(Optional.of(FavoriteFixture.TEST_FAVORITE));

        // when
        ItemResponse result = itemService.findById(TEST_USER.getId(), TEST_ITEM.getId());

        // then
        assertThat(result.getItemId()).isEqualTo(TEST_ITEM.getId());
        then(itemRepository).should(times(1)).findWithSellerAndItemImagesAndCategoryAndMajorById(any());
        then(favoriteRepository).should(times(1)).findByUserIdAndItemId(any(), any());
    }

    @Test
    @DisplayName("상품을 상세 조회할 때 상품이 존재하지 않으면 예외가 발생한다.")
    void findByIdNotFound() {
        // given
        given(itemRepository.findWithSellerAndItemImagesAndCategoryAndMajorById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> itemService.findById(TEST_USER.getId(), TEST_ITEM.getId()));

        // then
        then(itemRepository).should(times(1)).findWithSellerAndItemImagesAndCategoryAndMajorById(any());
    }

    @Test
    @DisplayName("상품을 검색한다.")
    void findBySearchRequest() {
        // given
        given(itemQueryRepository.findBySearchCondition(any(), any(), any(), any(), any(), any()))
                .willReturn(Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE));

        // when
        List<ItemResponse> result = itemService.findBySearchRequest(TEST_USER.getId(), TEST_ITEM_SEARCH_REQUEST);

        // then
        assertThat(result.size()).isEqualTo(1);
        then(itemQueryRepository).should(times(1)).findBySearchCondition(any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("판매자의 상품을 검색한다.")
    void findBySeller() {
        // given
        given(itemQueryRepository.findBySellerId(any()))
                .willReturn(Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE));

        // when
        List<ItemResponse> result = itemService.findBySeller(any());

        // then
        assertThat(result.size()).isEqualTo(1);
        then(itemQueryRepository).should(times(1)).findBySellerId(any());
    }
}