package inu.market.message.service;

import inu.market.client.FirebaseClient;
import inu.market.message.domain.Message;
import inu.market.message.domain.MessageRepository;
import inu.market.message.domain.MessageType;
import inu.market.message.dto.MessageRequest;
import inu.market.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public MessageResponse findLastByRoomId(Long roomId) {
        Message findMessage = messageRepository.findTopByRoomIdOrderByCreatedAtDesc(roomId)
                .orElse(Message.createMessage(0L, 0L, "", "", MessageType.TEXT));
        return MessageResponse.from(findMessage);
    }

    public List<MessageResponse> findByRoomId(Long roomId, int size, String lastMessageDate) {
        List<Message> messages = messageRepository
                .findAllByRoomIdAndCreatedAtIsBeforeOrderByCreatedAtDesc(
                        roomId, LocalDateTime.parse(lastMessageDate), PageRequest.of(0, size));
        return messages.stream()
                .map(message -> MessageResponse.from(message))
                .collect(Collectors.toList());
    }
}
