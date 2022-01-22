package inu.market.message.domain;

import inu.market.common.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageTypeTest {

    @Test
    @DisplayName("메세지 타입을 생성한다.")
    void from() {
        // given
        String messageType = "문자";

        // when
        MessageType result = MessageType.from(messageType);

        // then
        assertThat(result).isEqualTo(MessageType.TEXT);
    }

    @Test
    @DisplayName("메세지 타입을 생성한다.")
    void fromNotFound() {
        // given
        String messageType = "몰라";

        // when
        assertThrows(NotFoundException.class, () -> MessageType.from(messageType));
    }


    @Test
    @DisplayName("메세지 타입을 문자열로 얻는다.")
    void getType() {
        // when
        String result = MessageType.TEXT.getType();

        // then
        assertThat(result).isEqualTo("문자");
    }
}