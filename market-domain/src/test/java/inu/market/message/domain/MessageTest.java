package inu.market.message.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    @DisplayName("메세지를 생성한다.")
    void createMessage() {
        // given
        Long roomId = 1L;
        Long senderId = 1L;
        String nickName = "황주환";
        String content = "내용";
        MessageType messageType = MessageType.TEXT;

        // when
        Message result = Message.createMessage(roomId, senderId, nickName, content, messageType);

        // then
        assertThat(result.getRoomId()).isEqualTo(roomId);
        assertThat(result.getSenderId()).isEqualTo(senderId);
        assertThat(result.getNickName()).isEqualTo(nickName);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getMessageType()).isEqualTo(messageType);
    }

    @Test
    @DisplayName("메세지 알림을 보낸다.")
    void send() {
        // given
        Message message = Message.createMessage(1L, 1L, "황주환", "내용", MessageType.TEXT);

        // when
        Message result = message.send("pushToken");

        // then
        assertThat(result).isEqualTo(message);
    }
}