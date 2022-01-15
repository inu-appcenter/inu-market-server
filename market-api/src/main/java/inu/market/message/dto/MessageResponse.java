package inu.market.message.dto;

import inu.market.message.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private Long senderId;

    private String nickName;

    private String content;

    private String messageType;

    private LocalDateTime createdAt;

    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getSenderId(), message.getNickName(), message.getContent(),
                                   message.getMessageType().name(), message.getCreatedAt());
    }
}
