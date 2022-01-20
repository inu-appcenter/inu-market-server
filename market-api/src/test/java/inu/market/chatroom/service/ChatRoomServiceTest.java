package inu.market.chatroom.service;

import inu.market.chatroom.domain.ChatRoomRepository;
import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.item.domain.ItemRepository;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserResponse;
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

import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM;
import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM1;
import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("채팅방을 생성한다.")
    void create() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(itemRepository.findWithSellerById(any()))
                .willReturn(Optional.of(TEST_ITEM));

        given(chatRoomRepository.save(any()))
                .willReturn(TEST_CHAT_ROOM);

        // when
        Long result = chatRoomService.create(TEST_USER.getId(), TEST_ITEM.getId());

        // then
        assertThat(result).isEqualTo(TEST_CHAT_ROOM.getId());
    }

    @Test
    @DisplayName("채팅방 리스트를 조회한다.")
    void findBySellerOrBuyer() {
        // given
        given(chatRoomRepository.findWithItemAndBuyerAndSellerBySellerIdOrBuyerId(any()))
                .willReturn(Arrays.asList(TEST_CHAT_ROOM, TEST_CHAT_ROOM1));

        // when
        List<ChatRoomResponse> result = chatRoomService.findBySellerOrBuyer(TEST_USER.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구매자가 채팅방을 삭제한다.")
    void deleteByBuyer() {
        // given
        given(chatRoomRepository.findById(any()))
                .willReturn(Optional.of(TEST_CHAT_ROOM));

        willDoNothing().
                given(chatRoomRepository)
                .delete(any());

        // when
        chatRoomService.delete(TEST_USER.getId(), TEST_CHAT_ROOM.getId());

        // then
        then(chatRoomRepository).should(times(1)).findById(any());
        then(chatRoomRepository).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("판매자가 채팅방을 삭제한다.")
    void deleteBySeller() {
        // given
        given(chatRoomRepository.findById(any()))
                .willReturn(Optional.of(TEST_CHAT_ROOM));

        willDoNothing().
                given(chatRoomRepository)
                .delete(any());

        // when
        chatRoomService.delete(TEST_USER1.getId(), TEST_CHAT_ROOM.getId());

        // then
        then(chatRoomRepository).should(times(1)).findById(any());
        then(chatRoomRepository).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("구매자/판매자가 아닌 회원이 채팅방을 삭제하면 예외가 발생한다.")
    void deleteNotBySellerOrBuyer() {
        // given
        given(chatRoomRepository.findById(any()))
                .willReturn(Optional.of(TEST_CHAT_ROOM));

        // when
        assertThrows(AccessDeniedException.class, () -> chatRoomService.delete(3L, TEST_CHAT_ROOM.getId()));

        // then
        then(chatRoomRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("상품 ID 로 채팅방을 조회힌다.")
    void findByItemId() {
        // given
        given(chatRoomRepository.findWithBuyerByItemId(any()))
                .willReturn(Arrays.asList(TEST_CHAT_ROOM));

        // when
        List<UserResponse> result = chatRoomService.findByItemId(TEST_ITEM.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
        then(chatRoomRepository).should(times(1)).findWithBuyerByItemId(any());
    }

    @Test
    @DisplayName("채팅방을 상세 조회한다.")
    void findById() {
        // given
        given(chatRoomRepository.findWithItemAndBuyerAndSellerById(any()))
                .willReturn(Optional.of(TEST_CHAT_ROOM));

        // when
        ChatRoomResponse result = chatRoomService.findById(TEST_USER.getId(), TEST_CHAT_ROOM.getId());

        // then
        assertThat(result.getItem().getItemId()).isEqualTo(TEST_ITEM.getId());
        assertThat(result.getUser().getUserId()).isEqualTo(TEST_USER1.getId());
        then(chatRoomRepository).should(times(1)).findWithItemAndBuyerAndSellerById(any());
    }
}