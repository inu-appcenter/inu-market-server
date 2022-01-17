package inu.market.common;


import inu.market.message.domain.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageEvent extends ApplicationEvent {

    private Message message;

    private String pushToken;

    public MessageEvent(Object source) {
        super(source);
    }

    public static MessageEvent create(Message message, String pushToken) {
        MessageEvent messageEvent = new MessageEvent(message);
        messageEvent.message = message;
        messageEvent.pushToken = pushToken;
        return messageEvent;
    }

}
