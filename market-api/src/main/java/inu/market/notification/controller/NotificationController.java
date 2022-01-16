package inu.market.notification.controller;

import inu.market.config.LoginUser;
import inu.market.notification.dto.NotificationResponse;
import inu.market.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/api/notifications")
    public ResponseEntity<List<NotificationResponse>> findByUserId(@LoginUser Long userId,
                                                                   @RequestParam(required = false) Long notificationId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId, notificationId));
    }
}
