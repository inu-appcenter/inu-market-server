package inu.market.chatroom.service;

import inu.market.chatroom.domain.ChatRoom;
import inu.market.chatroom.domain.ChatRoomQueryRepository;
import inu.market.chatroom.domain.ChatRoomRepository;
import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomQueryRepository chatRoomQueryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(Long userId, Long itemId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Item findItem = itemRepository.findWithSellerById(itemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        ChatRoom chatRoom = ChatRoom.createChatRoom(findItem, findUser, findItem.getSeller());
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    public List<ChatRoomResponse> findBySellerOrBuyer(Long userId) {
        List<ChatRoom> chatRooms = chatRoomQueryRepository.findWithItemAndBuyerAndSellerBySellerIdOrBuyerId(userId);
        return chatRooms.stream()
                .map(chatRoom -> ChatRoomResponse.from(chatRoom, userId))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Long chatRoomId) {
        ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방입니다."));

        if (!findChatRoom.getBuyer().getId().equals(userId) || !findChatRoom.getSeller().getId().equals(userId)) {
            throw new RuntimeException("채팅방을 삭제할 수 없습니다.");
        }

        chatRoomRepository.delete(findChatRoom);
    }

    public List<UserResponse> findByItemId(Long itemId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findWithBuyerByItemId(itemId);
        return chatRooms.stream()
                .map(chatRoom -> UserResponse.from(chatRoom.getBuyer()))
                .collect(Collectors.toList());
    }
}
