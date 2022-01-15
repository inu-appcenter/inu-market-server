package inu.market.chatroom.controller;

import inu.market.chatroom.dto.ChatRoomResponse;
import inu.market.chatroom.service.ChatRoomService;
import inu.market.config.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/api/items/{itemId}/chatRooms")
    public ResponseEntity<ChatRoomResponse> create(@LoginUser Long userId,
                                                   @PathVariable Long itemId){
        return ResponseEntity.ok(chatRoomService.create(userId, itemId));
    }

}
