package inu.market.message.controller;

import inu.market.message.dto.MessageRequest;
import inu.market.message.dto.MessageResponse;
import inu.market.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat/message")
    public void message(MessageRequest request) {
        MessageResponse messageResponse = messageService.create(request);
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + request.getRoomId(), messageResponse);
    }

}
