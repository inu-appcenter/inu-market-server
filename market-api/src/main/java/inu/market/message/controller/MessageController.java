package inu.market.message.controller;

import inu.market.message.dto.MessageRequest;
import inu.market.message.dto.MessageResponse;
import inu.market.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/api/messages/imageUrls")
    public ResponseEntity<Map<String, String>> convertToImageUrl(@RequestPart MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new RuntimeException("업로드 할 이미지가 없습니다.");
        }

        Map<String, String> response = new HashMap<>();
        String imageUrl = messageService.uploadImage(image);
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);
    }

}
