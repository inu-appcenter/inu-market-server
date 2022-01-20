package inu.market.chatroom.domain;

import inu.market.item.domain.Item;
import inu.market.item.domain.Status;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChatRoomTest {

    @Test
    @DisplayName("채팅방을 생성한다.")
    void createChatRoom() {
        // given
        User buyer = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);

        // when
        ChatRoom result = ChatRoom.createChatRoom(item, buyer, seller);

        // then
        assertThat(result.getItem()).isEqualTo(item);
        assertThat(result.getBuyer()).isEqualTo(buyer);
        assertThat(result.getSeller()).isEqualTo(seller);
    }
}