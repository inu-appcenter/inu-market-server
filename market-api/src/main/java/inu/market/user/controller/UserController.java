package inu.market.user.controller;

import inu.market.common.NotExistException;
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

import static inu.market.common.NotExistException.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateRequest request) {
        String jwtToken = userService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request) {
        String jwtToken = userService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();
    }

    @PostMapping("/api/users/imageUrls")
    public ResponseEntity<Map<String, String>> convertToImageUrl(@RequestPart MultipartFile image) {

        if (image.isEmpty()) {
            throw new NotExistException(IMAGE_NOT_EXIST);
        }

        Map<String, String> response = new HashMap<>();
        String imageUrl = userService.uploadImage(image);
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/users/profile")
    public ResponseEntity<Void> updateProfile(@LoginUser Long userId,
                                              @RequestBody @Valid UserUpdateProfileRequest request) {
        userService.updateProfile(userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/users/profile")
    public ResponseEntity<UserResponse> findByLoginId(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

}
