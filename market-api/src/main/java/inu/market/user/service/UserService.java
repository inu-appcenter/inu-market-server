package inu.market.user.service;

import inu.market.client.AwsClient;
import inu.market.client.InuClient;
import inu.market.common.DuplicateException;
import inu.market.common.NotFoundException;
import inu.market.security.util.JwtUtil;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static inu.market.common.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final InuClient inuClient;
    private final AwsClient awsClient;
    private final JwtUtil jwtUtil;

    @Transactional
    public String create(UserCreateRequest request) {

        inuClient.login(request.getInuId(), request.getPassword());

        if (userRepository.findByInuId(request.getInuId()).isPresent()) {
            throw new DuplicateException("이미 회원가입한 회원입니다.");
        }

        User user = User.createUser(request.getInuId(), Role.ROLE_USER);
        userRepository.save(user);

        return jwtUtil.createToken(user.getId());
    }

    @Transactional
    public String login(UserLoginRequest request) {
        inuClient.login(request.getInuId(), request.getPassword());

        User findUser = userRepository.findByInuId(request.getInuId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        findUser.changePushToken(request.getPushToken());

        return jwtUtil.createToken(findUser.getId());
    }

    public String uploadImage(MultipartFile image) {
        return awsClient.upload(image);
    }

    @Transactional
    public void updateProfile(Long userId, UserUpdateProfileRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        findUser.changeProfile(request.getNickName(), request.getImageUrl());
    }

    public UserResponse findById(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserResponse.from(findUser);
    }
}
