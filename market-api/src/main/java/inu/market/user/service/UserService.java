package inu.market.user.service;

import inu.market.client.InuClient;
import inu.market.security.util.JwtUtil;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserCreateRequest;
import inu.market.user.dto.UserLoginRequest;
import inu.market.user.dto.UserResponse;
import inu.market.user.dto.UserUpdateNickNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService  {

    private final UserRepository userRepository;
    private final InuClient inuClient;
    private final JwtUtil jwtUtil;

    @Transactional
    public String create(UserCreateRequest request) {

        inuClient.login(request.getInuId(), request.getPassword());

        if (userRepository.findByInuId(request.getInuId()).isPresent()) {
            throw new RuntimeException("이미 회원가입한 회원입니다.");
        }

        User user = User.createUser(request.getInuId(), Role.ROLE_USER);
        userRepository.save(user);

        return jwtUtil.createToken(user.getId());
    }

    @Transactional
    public String login(UserLoginRequest request) {
        inuClient.login(request.getInuId(), request.getPassword());

        User findUser = userRepository.findByInuId(request.getInuId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        findUser.changePushToken(request.getPushToken());

        return jwtUtil.createToken(findUser.getId());
    }

    @Transactional
    public UserResponse updateNickName(Long userId, UserUpdateNickNameRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        findUser.changeNickName(request.getNickName());

        return UserResponse.of(findUser);
    }
}
