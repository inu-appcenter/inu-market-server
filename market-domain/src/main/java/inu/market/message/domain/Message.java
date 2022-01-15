package inu.market.message.domain;

import inu.market.chatroom.domain.ChatRoom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    private Long roomId;

    private Long senderId;

    private String nickName;

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Message createMessage(Long roomId, Long senderId, String nickName, String content, MessageType messageType) {
        Message message = new Message();
        message.roomId =roomId;
        message.senderId = senderId;
        message.nickName = nickName;
        message.content = content;
        message.messageType = messageType;
        return message;
    }

}
