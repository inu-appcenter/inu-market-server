package inu.market.chatroom.service;

import inu.market.chatroom.domain.ChatRoom;
import inu.market.chatroom.domain.ChatRoomRepository;
import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponse create(Long userId, Long itemId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Item findItem = itemRepository.findWithSellerById(itemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        ChatRoom chatRoom = ChatRoom.createChatRoom(findItem, findUser, findItem.getSeller());
        chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.from(chatRoom);
    }
}
