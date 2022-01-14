package inu.market.user.controller;

import inu.market.config.LoginUser;
import inu.market.user.dto.UserLoginRequest;
import inu.market.user.dto.UserCreateRequest;
import inu.market.user.dto.UserResponse;
import inu.market.user.dto.UserUpdateNickNameRequest;
import inu.market.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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

    @PatchMapping("/api/users/nickname")
    public ResponseEntity<UserResponse> updateNickName(@LoginUser Long userId,
                                                       @RequestBody @Valid UserUpdateNickNameRequest request) {
        return ResponseEntity.ok(userService.updateNickName(userId, request));
    }

    @PatchMapping("/api/users/image")
    public ResponseEntity<UserResponse> updateImage(@LoginUser Long userId,
                                                    @RequestPart MultipartFile file){
        return ResponseEntity.ok(userService.updateImage(userId, file));
    }



}
