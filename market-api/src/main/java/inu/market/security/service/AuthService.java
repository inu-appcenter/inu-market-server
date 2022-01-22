package inu.market.security.service;

import inu.market.common.NotFoundException;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User findUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotFoundException(userId + "는 존재하지 않는 회원 ID 입니다."));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(String.valueOf(findUser.getRole())));

        return new org.springframework.security.core.userdetails.User(String.valueOf(findUser.getId()), "", authorities);
    }
}
