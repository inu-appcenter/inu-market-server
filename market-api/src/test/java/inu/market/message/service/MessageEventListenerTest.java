package inu.market.message.service;

import inu.market.client.FirebaseClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static inu.market.message.MessageFixture.TEST_MESSAGE_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class MessageEventListenerTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private FirebaseClient firebaseClient;

    @Test
    @DisplayName("메세지 전송 Push 알림을 보낸다.")
    void handleMessageEvent() {
        // given
        willDoNothing()
                .given(firebaseClient)
                .send(any(), any(), any());

        // when
        messageService.create(TEST_MESSAGE_REQUEST);

        // then
        then(firebaseClient).should(times(1)).send(any(), any(), any());
    }
}