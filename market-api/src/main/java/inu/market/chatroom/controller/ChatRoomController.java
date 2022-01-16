package inu.market.chatroom.controller;

import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.chatroom.service.ChatRoomService;
import inu.market.config.LoginUser;
import inu.market.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/api/items/{itemId}/chat/rooms")
    public ResponseEntity<Map<String, Long>> create(@LoginUser Long userId,
                                                    @PathVariable Long itemId) {
        Long roomId = chatRoomService.create(userId, itemId);
        Map<String, Long> response = new HashMap<>();
        response.put("roomId", roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatRoomResponse>> findBySellerOrBuyer(@LoginUser Long userId) {
        return ResponseEntity.ok(chatRoomService.findBySellerOrBuyer(userId));
    }

    @DeleteMapping("/api/chat/rooms/{roomId}")
    public ResponseEntity<Void> delete(@LoginUser Long userId, @PathVariable Long roomId) {
        chatRoomService.delete(userId, roomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/items/{itemId}/chat/rooms/buyers")
    public ResponseEntity<List<UserResponse>> findByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(chatRoomService.findByItemId(itemId));
    }

}
