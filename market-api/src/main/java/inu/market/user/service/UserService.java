package inu.market.user.service;

import inu.market.auth.JwtUtil;
import inu.market.client.InuClient;
import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final InuClient inuClient;
    private final JwtUtil jwtUtil;

    @Transactional
    public String create(UserCreateRequest request) {

        inuClient.login(request.getInuId(), request.getPassword());

        if(userRepository.findByInuId(request.getInuId()).isPresent()){
            throw new RuntimeException("이미 회원가입한 회원입니다.");
        }

        User user = User.createUser(request.getInuId(), Role.USER);
        userRepository.save(user);

        return jwtUtil.createToken(user.getId());
    }

}
