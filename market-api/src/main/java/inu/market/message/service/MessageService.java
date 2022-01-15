package inu.market.message.service;

import inu.market.client.FirebaseClient;
import inu.market.message.domain.Message;
import inu.market.message.domain.MessageRepository;
import inu.market.message.domain.MessageType;
import inu.market.message.dto.MessageRequest;
import inu.market.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final FirebaseClient firebaseClient;

    @Transactional
    public MessageResponse create(MessageRequest request) {
        Message message = Message.createMessage(request.getRoomId(), request.getSenderId(), request.getNickName(),
                                                request.getContent(), MessageType.valueOf(request.getMessageType()));

        firebaseClient.send(request.getPushToken(), request.getNickName(), request.getContent());
        messageRepository.save(message);
        return MessageResponse.from(message);
    }


}
