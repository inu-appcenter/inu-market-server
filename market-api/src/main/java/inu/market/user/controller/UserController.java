package inu.market.user.controller;

import inu.market.config.LoginUser;
import inu.market.user.dto.*;
import inu.market.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateRequest request) {
        String jwtToken = userService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + jwtToken)
                .build();
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request) {
        String jwtToken = userService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + jwtToken)
                .build();
    }

    @PostMapping("/api/users/imageUrls")
    public ResponseEntity<Map<String, String>> convertToImageUrl(@RequestPart MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new RuntimeException("업로드 할 이미지가 없습니다.");
        }

        Map<String, String> response = new HashMap<>();
        String imageUrl = userService.uploadImage(image);
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/users/profile")
    public ResponseEntity<Void> updateProfile(@LoginUser Long userId,
                                              @RequestBody @Valid UserUpdateProfileRequest request) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PatchMapping("/api/users/notification")
    public ResponseEntity<Void> updateNotification(@LoginUser Long userId,
                                                   @RequestBody @Valid UserUpdateNotificationRequest notificationRequest) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/users/profile")
    public ResponseEntity<UserResponse> findByLoginId(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

}
