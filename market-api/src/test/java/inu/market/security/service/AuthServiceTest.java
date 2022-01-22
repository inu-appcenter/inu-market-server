package inu.market.security.service;

import inu.market.common.NotFoundException;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static inu.market.user.UserFixture.TEST_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원을 조회한다.")
    void loadUserByUsername() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        // when
        UserDetails result = authService.loadUserByUsername(String.valueOf(TEST_USER.getId()));

        // then
        assertThat(result.getUsername()).isEqualTo(String.valueOf(TEST_USER.getId()));
        assertThat(result.getAuthorities()).hasSize(1);
        then(userRepository).should(times(1)).findById(any());
    }

    @Test()
    @DisplayName("회원을 조회하고 없으면 예외가 발생한다.")
    void loadUserByUsernameNotFound() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> authService.loadUserByUsername(String.valueOf(TEST_USER.getId())));

        // then
        then(userRepository).should(times(1)).findById(any());
    }
}