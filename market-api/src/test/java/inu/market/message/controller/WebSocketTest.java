package inu.market.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import inu.market.CommonFixture;
import inu.market.message.dto.MessageResponse;
import inu.market.message.service.MessageService;
import inu.market.security.util.JwtUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

import static inu.market.CommonFixture.TEST_AUTHORIZATION;
import static inu.market.message.MessageFixture.TEST_MESSAGE_REQUEST;
import static inu.market.message.MessageFixture.TEST_MESSAGE_RESPONSE;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {

    @LocalServerPort
    private int port;

    @MockBean
    private MessageService messageService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private WebSocketStompClient webSocketStompClient;

    private StompSession session;

    private BlockingQueue<String> messageResponses;

    @BeforeEach
    void setUp() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);
        webSocketStompClient = new WebSocketStompClient(sockJsClient);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        messageResponses = new LinkedBlockingDeque<>();
    }

    @Test
    @DisplayName("채팅 메세지를 전송한다.")
    void sendMessage() throws Exception {
        // given
        given(jwtUtil.isValidToken(any()))
                .willReturn(true);

        given(messageService.create(any()))
                .willReturn(TEST_MESSAGE_RESPONSE);

        // when
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION);
        session = webSocketStompClient
                .connect("ws://localhost:" + port + "/ws-stomp", null, stompHeaders, new StompSessionHandlerAdapter() {
                }, new Object[0]).get(60, SECONDS);

        stompHeaders = new StompHeaders();
        stompHeaders.add(StompHeaders.DESTINATION, String.format("/sub/chat/rooms/%s", 1L));
        stompHeaders.add(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION);
        session.subscribe(stompHeaders, new StompFrameHandlerImpl<>(messageResponses));

        stompHeaders = new StompHeaders();
        stompHeaders.add(StompHeaders.DESTINATION, String.format("/pub/chat/messages"));
        stompHeaders.add(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION);
        session.send(stompHeaders, TEST_MESSAGE_REQUEST);

        String content = messageResponses.poll(5, SECONDS);
        MessageResponse result = objectMapper.readValue(content, MessageResponse.class);

        // then
        assertThat(result.getContent()).isEqualTo(TEST_MESSAGE_RESPONSE.getContent());
        assertThat(result.getSenderId()).isEqualTo(TEST_MESSAGE_RESPONSE.getSenderId());
        assertThat(result.getNickName()).isEqualTo(TEST_MESSAGE_RESPONSE.getNickName());
        assertThat(result.getMessageType()).isEqualTo(TEST_MESSAGE_RESPONSE.getMessageType());
        assertThat(result.getCreatedAt()).isNotNull();
        then(jwtUtil).should(times(1)).isValidToken(any());
        then(messageService).should(times(1)).create(any());
    }

    @Test
    @DisplayName("예외 테스트: 비회원이 채팅 메세지를 전송하면 예외가 발생한다.")
    void sendMessageNotUser() throws Exception {
        // when
        assertThrows(ExecutionException.class,
                () -> webSocketStompClient.connect("ws://localhost:" + port + "/ws-stomp", null, null, new StompSessionHandlerAdapter() {
                        },
                        new Object[0]).get(60, SECONDS));
    }

    @Test
    @DisplayName("예외 테스트: 비회원이 채팅 메세지를 전송하면 예외가 발생한다.")
    void sendMessageNotBearerToken() throws Exception {

        // given
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add(HttpHeaders.AUTHORIZATION, CommonFixture.TEST_EXPIRATION_TOKEN);

        // when
        assertThrows(ExecutionException.class,
                () -> webSocketStompClient.connect("ws://localhost:" + port + "/ws-stomp", null, stompHeaders, new StompSessionHandlerAdapter() {
                        },
                        new Object[0]).get(60, SECONDS));
    }

    static class StompFrameHandlerImpl<T> implements StompFrameHandler {

        private ObjectMapper objectMapper;
        private BlockingQueue<String> messageResponses;

        public StompFrameHandlerImpl(BlockingQueue<String> messageResponses) {
            this.objectMapper = new ObjectMapper();
            this.messageResponses = messageResponses;
        }

        // payload 를 받을 클래스 타입을 지정
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
        }

        // payload 가 담길 BlockingQueue 지정
        @SneakyThrows
        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            String content = new String((byte[]) payload);
            messageResponses.offer(content);
        }
    }

}
