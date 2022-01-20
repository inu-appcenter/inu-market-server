package inu.market.chatroom;

import inu.market.chatroom.domain.ChatRoom;
import inu.market.chatroom.dto.ChatRoomResponse;

import static inu.market.item.ItemFixture.*;
import static inu.market.user.UserFixture.*;

public class ChatRoomFixture {

    public static final ChatRoom TEST_CHAT_ROOM = new ChatRoom(1L, TEST_ITEM, TEST_USER, TEST_USER1);
    public static final ChatRoom TEST_CHAT_ROOM1 = new ChatRoom(2L, TEST_ITEM, TEST_USER1, TEST_USER);

    public static final ChatRoomResponse TEST_CHAT_ROOM_RESPONSE
            = new ChatRoomResponse(TEST_CHAT_ROOM.getId(), TEST_ITEM_SIMPLE_RESPONSE, TEST_USER_RESPONSE);
}
