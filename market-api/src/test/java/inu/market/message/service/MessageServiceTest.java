package inu.market.message.service;

import inu.market.client.AwsClient;
import inu.market.message.domain.MessageRepository;
import inu.market.message.dto.MessageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.CommonFixture.*;
import static inu.market.chatroom.ChatRoomFixture.TEST_CHAT_ROOM;
import static inu.market.message.MessageFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private AwsClient awsClient;

    @Test
    @DisplayName("메세지를 생성한다.")
    void create() {
        // given
        given(messageRepository.save(any()))
                .willReturn(null);

        // when
        MessageResponse result = messageService.create(TEST_MESSAGE_REQUEST);

        // then
        assertThat(result.getNickName()).isEqualTo(TEST_MESSAGE_REQUEST.getNickName());
        assertThat(result.getContent()).isEqualTo(TEST_MESSAGE_REQUEST.getContent());
        assertThat(result.getSenderId()).isEqualTo(TEST_MESSAGE_REQUEST.getSenderId());
        assertThat(result.getMessageType()).isEqualTo(TEST_MESSAGE_REQUEST.getMessageType());
        then(messageRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("채팅방의 마지막 메세지를 조회한다.")
    void findLastByRoomId() {
        // given
        given(messageRepository.findTopByRoomIdOrderByCreatedAtDesc(any()))
                .willReturn(Optional.of(TEST_MESSAGE));

        // when
        MessageResponse result = messageService.findLastByRoomId(TEST_CHAT_ROOM.getId());

        // then
        assertThat(result.getNickName()).isEqualTo(TEST_MESSAGE.getNickName());
        assertThat(result.getContent()).isEqualTo(TEST_MESSAGE.getContent());
        assertThat(result.getSenderId()).isEqualTo(TEST_MESSAGE.getSenderId());
        assertThat(result.getMessageType()).isEqualTo(TEST_MESSAGE.getMessageType().getType());
        assertThat(result.getCreatedAt()).isEqualTo(TEST_MESSAGE.getCreatedAt());
        then(messageRepository).should(times(1)).findTopByRoomIdOrderByCreatedAtDesc(any());
    }

    @Test
    @DisplayName("채팅방의 메세지를 조회해서 없을 경우 빈 메세지를 반환한다.")
    void findLastByRoomIdNotExist() {
        // given
        given(messageRepository.findTopByRoomIdOrderByCreatedAtDesc(any()))
                .willReturn(Optional.empty());

        // when
        MessageResponse result = messageService.findLastByRoomId(TEST_CHAT_ROOM.getId());

        // then
        assertThat(result.getNickName()).isEmpty();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getSenderId()).isZero();
        assertThat(result.getMessageType()).isEqualTo(TEST_MESSAGE_TYPE.getType());
        then(messageRepository).should(times(1)).findTopByRoomIdOrderByCreatedAtDesc(any());
    }

    @Test
    @DisplayName("채팅방의 메세지를 조회한다.")
    void findByRoomId() {
        // given
        given(messageRepository.findAllByRoomIdAndCreatedAtIsBeforeOrderByCreatedAtDesc(any(), any(), any()))
                .willReturn(Arrays.asList(TEST_MESSAGE));

        // when
        List<MessageResponse> result = messageService.findByRoomId(TEST_CHAT_ROOM.getId(), TEST_SIZE, LocalDateTime.now().toString());

        // then
        assertThat(result.size()).isEqualTo(1);
        then(messageRepository).should(times(1)).findAllByRoomIdAndCreatedAtIsBeforeOrderByCreatedAtDesc(any(), any(), any());
    }

    @Test
    @DisplayName("메세지 이미지를 업로드한다.")
    void uploadImage() {
        // given
        given(awsClient.upload(any()))
                .willReturn(TEST_IMAGE_URL);

        // when
        String result = messageService.uploadImage(TEST_IMAGE_FILE);

        // then
        assertThat(result).isEqualTo(TEST_IMAGE_URL);
        then(awsClient).should(times(1)).upload(any());
    }
}