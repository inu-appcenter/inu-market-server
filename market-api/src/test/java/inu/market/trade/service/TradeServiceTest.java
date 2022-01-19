package inu.market.trade.service;

import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.item.query.ItemQueryRepository;
import inu.market.notification.domain.NotificationRepository;
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

import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.item.ItemFixture.TEST_ITEM_SIMPLE_RESPONSE;
import static inu.market.trade.TradeFixture.TEST_TRADE_CREATE_REQUEST;
import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ItemQueryRepository itemQueryRepository;

    @Test
    @DisplayName("거래를 생성한다.")
    void create() {
        // given
        given(itemRepository.findWithSellerById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(tradeRepository.save(any()))
                .willReturn(null);

        given(notificationRepository.save(any()))
                .willReturn(null);

        // when
        tradeService.create(TEST_USER.getId(), TEST_TRADE_CREATE_REQUEST);

        // then
        then(itemRepository).should(times(1)).findWithSellerById(any());
        then(userRepository).should(times(1)).findById(any());
        then(tradeRepository).should(times(1)).save(any());
        then(notificationRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("거래를 생성한다.")
    void createNotOwner() {
        // given
        given(itemRepository.findWithSellerById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        // when
        assertThrows(AccessDeniedException.class, () -> tradeService.create(TEST_USER1.getId(), TEST_TRADE_CREATE_REQUEST));

        // then
        then(itemRepository).should(times(1)).findWithSellerById(any());
    }

    @Test
    @DisplayName("구매한 상품을 조회한다.")
    void findByBuyerId() {
        // given
        given(itemQueryRepository.findByTradeBuyerId(any()))
                .willReturn(Arrays.asList(TEST_ITEM_SIMPLE_RESPONSE));

        // when
        List<ItemResponse> result = tradeService.findByBuyerId(TEST_USER.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}