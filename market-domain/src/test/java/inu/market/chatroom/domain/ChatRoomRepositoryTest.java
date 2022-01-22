package inu.market.chatroom.domain;

import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.domain.Status;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ChatRoomRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        chatRoomRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 ID 로 채팅방과 판매자를 같이 조회한다.")
    void findWithBuyerByItemId() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        ChatRoom chatRoom = ChatRoom.createChatRoom(item, user, seller);
        chatRoomRepository.save(chatRoom);

        // when
        List<ChatRoom> result = chatRoomRepository.findWithBuyerByItemId(item.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품으로 채팅방을 모두 삭제한다.")
    void deleteAllByItem() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        ChatRoom chatRoom = ChatRoom.createChatRoom(item, user, seller);
        chatRoomRepository.save(chatRoom);

        // when
        chatRoomRepository.deleteAllByItem(item);

        // then
        List<ChatRoom> result = chatRoomRepository.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    @DisplayName("회원 ID 로 채팅방과 상품/판매자/구매자를 같이 조회한다.")
    void findWithItemAndBuyerAndSellerBySellerIdOrBuyerId() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        ChatRoom chatRoom = ChatRoom.createChatRoom(item, user, seller);
        chatRoomRepository.save(chatRoom);

        // when
        List<ChatRoom> result = chatRoomRepository.findWithItemAndBuyerAndSellerBySellerIdOrBuyerId(user.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("채팅방과 상품/판매자/구매자를 같이 조회한다.")
    void findWithItemAndBuyerAndSellerById() {
        // given
        User user = User.createUser(201601758, Role.ROLE_USER);
        User seller = User.createUser(201601757, Role.ROLE_USER);
        userRepository.save(user);
        userRepository.save(seller);

        Item item = Item.createItem("제목", "내용", "이미지 URL", 1000, Status.SALE, seller);
        itemRepository.save(item);

        ChatRoom chatRoom = ChatRoom.createChatRoom(item, user, seller);
        chatRoomRepository.save(chatRoom);

        // when
        ChatRoom result = chatRoomRepository.findWithItemAndBuyerAndSellerById(item.getId()).get();

        // then
        assertThat(result.getItem()).isEqualTo(item);
        assertThat(result.getBuyer()).isEqualTo(user);
        assertThat(result.getSeller()).isEqualTo(seller);
    }
}