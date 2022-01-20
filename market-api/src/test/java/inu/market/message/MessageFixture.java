package inu.market.message;

import inu.market.message.domain.Message;
import inu.market.message.domain.MessageType;
import inu.market.message.dto.MessageRequest;
import inu.market.message.dto.MessageResponse;

import java.time.LocalDateTime;

import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM;
import static inu.market.user.UserFixture.TEST_USER;

public class MessageFixture {

    public static final String TEST_MESSAGE_CONTENT = "내용";
    public static final MessageType TEST_MESSAGE_TYPE = MessageType.TEXT;

    public static final Message TEST_MESSAGE
            = new Message(1L, TEST_CHAT_ROOM.getId(), TEST_USER.getId(), TEST_USER.getNickName(),
            TEST_MESSAGE_CONTENT, MessageType.TEXT, LocalDateTime.now());

    public static final MessageResponse TEST_MESSAGE_RESPONSE = MessageResponse.from(TEST_MESSAGE);

    public static final MessageRequest TEST_MESSAGE_REQUEST
            = new MessageRequest(TEST_CHAT_ROOM.getId(), TEST_USER.getId(), TEST_USER.getNickName(),
            TEST_MESSAGE_CONTENT, TEST_MESSAGE_TYPE.getType(), TEST_USER.getPushToken());
}
