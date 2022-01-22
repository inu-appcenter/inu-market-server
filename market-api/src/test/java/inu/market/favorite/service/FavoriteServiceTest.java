package inu.market.favorite.service;

import inu.market.common.NotFoundException;
import inu.market.favorite.domain.FavoriteRepository;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.item.query.ItemQueryRepository;
import inu.market.notification.domain.NotificationRepository;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.favorite.FavoriteFixture.*;
import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.item.ItemFixture.TEST_ITEM_RESPONSE;
import static inu.market.user.UserFixture.TEST_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ItemQueryRepository itemQueryRepository;

    @Test
    @DisplayName("찜을 생성한다.")
    void create() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(itemRepository.findWithSellerById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(favoriteRepository.save(any()))
                .willReturn(null);

        given(notificationRepository.save(any()))
                .willReturn(null);

        // when
        favoriteService.create(TEST_USER.getId(), TEST_FAVORITE_CREATE_REQUEST);

        // then
        then(userRepository).should(times(1)).findById(any());
        then(itemRepository).should(times(1)).findWithSellerById(any());
        then(favoriteRepository).should(times(1)).save(any());
        then(notificationRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("찜을 생성할 때 회원이 존재하지 않으면 예외가 발생한다.")
    void createUserNotFound() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> favoriteService.create(TEST_USER.getId(), TEST_FAVORITE_CREATE_REQUEST));

        // then
        then(userRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("찜을 생성할 때 상품이 존재하지 않으면 예외가 발생한다.")
    void createItemNotFound() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(itemRepository.findWithSellerById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> favoriteService.create(TEST_USER.getId(), TEST_FAVORITE_CREATE_REQUEST));

        // then
        then(userRepository).should(times(1)).findById(any());
        then(itemRepository).should(times(1)).findWithSellerById(any());
    }

    @Test
    @DisplayName("찜을 취소한다.")
    void delete() {
        // given
        given(favoriteRepository.findWithItemByUserIdAndItemId(any(), any()))
                .willReturn(Optional.of(TEST_FAVORITE));

        willDoNothing()
                .given(favoriteRepository)
                .delete(any());
        // when
        favoriteService.delete(TEST_USER.getId(), TEST_FAVORITE_DELETE_REQUEST);

        // then
        then(favoriteRepository).should(times(1)).findWithItemByUserIdAndItemId(any(), any());
        then(favoriteRepository).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("찜을 취소할 때 찜이 존재하지 않으면 예외가 발생한다.")
    void deleteNotFound() {
        // given
        given(favoriteRepository.findWithItemByUserIdAndItemId(any(), any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> favoriteService.delete(TEST_USER.getId(), TEST_FAVORITE_DELETE_REQUEST));

        // then
        then(favoriteRepository).should(times(1)).findWithItemByUserIdAndItemId(any(), any());
    }

    @Test
    @DisplayName("찜 목록을 조회한다.")
    void findByUserId() {
        // given
        given(itemQueryRepository.findByFavoriteUserId(any()))
                .willReturn(Arrays.asList(TEST_ITEM_RESPONSE));

        // when
        List<ItemResponse> result = favoriteService.findByUserId(TEST_USER.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}