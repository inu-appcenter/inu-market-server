package inu.market.message.service;

import inu.market.client.FirebaseClient;
import inu.market.common.MessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MessageEventListener {

    private final FirebaseClient firebaseClient;

    @TransactionalEventListener
    public void handleMessageEvent(MessageEvent event) {
        firebaseClient.send(event.getPushToken(), event.getMessage().getNickName(), event.getMessage().getContent());
    }
}
