package inu.market.notification.service;

import inu.market.client.FirebaseClient;
import inu.market.favorite.service.FavoriteService;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static inu.market.CommonFixture.*;
import static inu.market.favorite.FavoriteFixture.TEST_FAVORITE_CREATE_REQUEST;
import static inu.market.item.ItemFixture.*;
import static inu.market.user.UserFixture.TEST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;

@SpringBootTest
class NotificationEventListenerTest {

    @Autowired
    private FavoriteService favoriteService;

    @MockBean
    private FirebaseClient firebaseClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("좋아요 Push 알림을 보낸다")
    void handleNotification() {
        // given
        User user = userRepository.save(User.createUser(201601757, Role.ROLE_USER));
        itemRepository.save(Item.createItem(TEST_ITEM_TITLE, TEST_ITEM_CONTENTS, TEST_IMAGE_URL, TEST_ITEM_PRICE, TEST_ITEM_STATUS, user));

        willDoNothing()
                .given(firebaseClient)
                .send(any(), any(), any());

        // when
        favoriteService.create(TEST_USER.getId(), TEST_FAVORITE_CREATE_REQUEST);

        // then
        then(firebaseClient).should(times(1)).send(any(), any(), any());
    }
}