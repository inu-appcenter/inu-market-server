package inu.market.message.controller;

import inu.market.message.dto.MessageRequest;
import inu.market.message.dto.MessageResponse;
import inu.market.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat/messages")
    public void message(MessageRequest request) {
        MessageResponse messageResponse = messageService.create(request);
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + request.getRoomId(), messageResponse);
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages/new")
    public ResponseEntity<MessageResponse> findLastByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(messageService.findLastByRoomId(roomId));
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageResponse>> findByRoomId(@PathVariable Long roomId,
                                                              @RequestParam int size, @RequestParam String lastMessageDate) {
        return ResponseEntity.ok(messageService.findByRoomId(roomId, size, lastMessageDate));
    }

}
