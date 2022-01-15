package inu.market.chatroom.dto;

import inu.market.chatroom.domain.ChatRoom;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {

    private ItemResponse item;

    private UserResponse seller;

    private UserResponse buyer;

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        ChatRoomResponse chatRoomResponse = new ChatRoomResponse();
        chatRoomResponse.item = ItemResponse.from(chatRoom.getItem());
        chatRoomResponse.seller = UserResponse.from(chatRoom.getSeller());
        chatRoomResponse.buyer = UserResponse.from(chatRoom.getBuyer());
        return chatRoomResponse;
    }
}
