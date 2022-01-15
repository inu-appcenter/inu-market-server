package inu.market.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private Long roomId;

    private Long senderId;

    private String nickName;

    private String content;

    private String messageType;

    private String pushToken;
}
