package inu.market.message.domain;

import inu.market.common.MessageEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Message extends AbstractAggregateRoot<Message> {

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
        message.roomId = roomId;
        message.senderId = senderId;
        message.nickName = nickName;
        message.content = content;
        message.messageType = messageType;
        return message;
    }

    public Message send(String pushToken) {
        this.registerEvent(MessageEvent.create(this, pushToken));
        return this;
    }

}
