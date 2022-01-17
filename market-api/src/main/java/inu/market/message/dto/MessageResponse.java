package inu.market.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getSenderId(), message.getNickName(), message.getContent(),
                                   message.getMessageType().getType(), message.getCreatedAt());
    }
}
