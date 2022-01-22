package inu.market.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FirebaseClientTest {

    @Autowired
    private FirebaseClient firebaseClient;

    @Test
    @DisplayName("Push 알림을 전송할 때 토큰이 올바르지 않다면 전송되지 않는다..")
    void send() {
        // given
        String content = "내용";
        String title = "제목";
        String pushToken = "pushToken";

        // when
        firebaseClient.send(pushToken, title, content);
    }

}