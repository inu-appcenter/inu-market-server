package inu.market.message.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
    }

    @Test
    @DisplayName("채팅방의 가장 최근 채팅을 찾는다.")
    void findTopByRoomIdOrderByCreatedAtDesc() {
        // given
        Message message1 = new Message(null, 1L, 1L, "황주환", "내용", MessageType.TEXT, LocalDateTime.now());
        messageRepository.save(message1);

        Message message2 = new Message(null, 1L, 1L, "황주환", "내용", MessageType.TEXT, LocalDateTime.now());
        messageRepository.save(message2);

        // when
        Message result = messageRepository.findTopByRoomIdOrderByCreatedAtDesc(1L).get();

        // then
        assertThat(result).isEqualTo(message2);
    }

    @Test
    @DisplayName("채팅방의 채팅을 조회한다.")
    void findAllByRoomIdAndCreatedAtIsBeforeOrderByCreatedAtDesc() {
        // given
        Message message1 = new Message(null, 1L, 1L, "황주환", "내용", MessageType.TEXT, LocalDateTime.now());
        messageRepository.save(message1);

        Message message2 = new Message(null, 1L, 1L, "황주환", "내용", MessageType.TEXT, LocalDateTime.now());
        messageRepository.save(message2);

        // when
        List<Message> result = messageRepository.findAllByRoomIdAndCreatedAtIsBeforeOrderByCreatedAtDesc(1L, LocalDateTime.now(), PageRequest.of(0, 10));

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}