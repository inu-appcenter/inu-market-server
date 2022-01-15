package inu.market.chatroom.controller;

import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.chatroom.service.ChatRoomService;
import inu.market.config.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/api/items/{itemId}/chatRooms")
    public ResponseEntity<ChatRoomResponse> create(@LoginUser Long userId,
                                                   @PathVariable Long itemId) {
        return ResponseEntity.ok(chatRoomService.create(userId, itemId));
    }

    @PatchMapping("/api/chatRooms/{chatRoomId}")
    public ResponseEntity<Void> exit(@LoginUser Long userId, @PathVariable Long chatRoomId) {
        chatRoomService.exit(userId, chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @GetMapping("/api/chatRooms")
//    public ResponseEntity<List<ChatRoomResponse>> findBySellerOrBuyer(@LoginUser Long userId) {
//
//    }


}
