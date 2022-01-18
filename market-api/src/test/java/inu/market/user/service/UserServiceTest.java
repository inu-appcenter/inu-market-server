package inu.market.user.service;

import inu.market.client.AwsClient;
import inu.market.client.InuClient;
import inu.market.common.DuplicateException;
import inu.market.security.util.JwtUtil;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static inu.market.CommonFixture.*;
import static inu.market.user.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AwsClient awsClient;

    @Mock
    private InuClient inuClient;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원 가입을 한다.")
    void create() {
        // given
        willDoNothing()
                .given(inuClient)
                .login(any(), any());

        given(userRepository.findByInuId(any()))
                .willReturn(Optional.empty());

        given(userRepository.save(any()))
                .willReturn(TEST_USER);

        given(jwtUtil.createToken(any()))
                .willReturn(TEST_AUTHORIZATION);

        // when
        String result = userService.create(TEST_USER_CREATE_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_AUTHORIZATION);

        then(inuClient).should(times(1)).login(any(), any());
        then(userRepository).should(times(1)).findByInuId(any());
        then(userRepository).should(times(1)).save(any());
        then(jwtUtil).should(times(1)).createToken(any());
    }

    @Test
    @DisplayName("중복 회원 가입을 하면 예외가 발생한다.")
    void createDuplicate() {
        // given
        willDoNothing()
                .given(inuClient)
                .login(any(), any());

        given(userRepository.findByInuId(any()))
                .willReturn(Optional.of(TEST_USER));

        // when
        assertThrows(DuplicateException.class, () -> userService.create(TEST_USER_CREATE_REQUEST));

        then(inuClient).should(times(1)).login(any(), any());
        then(userRepository).should(times(1)).findByInuId(any());
    }

    @Test
    @DisplayName("회원 로그인을 한다.")
    void login() {
        // given
        willDoNothing()
                .given(inuClient)
                .login(any(), any());

        given(userRepository.findByInuId(any()))
                .willReturn(Optional.of(TEST_USER));

        given(jwtUtil.createToken(any()))
                .willReturn(TEST_AUTHORIZATION);

        // when
        String result = userService.login(TEST_USER_LOGIN_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_AUTHORIZATION);

        then(inuClient).should(times(1)).login(any(), any());
        then(userRepository).should(times(1)).findByInuId(any());
        then(jwtUtil).should(times(1)).createToken(any());
    }

    @Test
    @DisplayName("이미지 업로드를 한다.")
    void uploadImage() {
        // given
        given(awsClient.upload(any()))
                .willReturn(TEST_IMAGE_URL);

        // when
        String result = userService.uploadImage(TEST_IMAGE_FILE);

        // then
        assertThat(result).isEqualTo(TEST_IMAGE_URL);

        then(awsClient).should(times(1)).upload(any());
    }

    @Test
    @DisplayName("프로필을 수정한다.")
    void updateProfile() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        // when
        userService.updateProfile(TEST_USER.getId(), TEST_USER_UPDATE_PROFILE_REQUEST);

        // then
        then(userRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("프로필을 조회한다.")
    void findById() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        // when
        UserResponse result = userService.findById(TEST_USER.getId());

        // then
        assertThat(result.getUserId()).isEqualTo(TEST_USER.getId());
        assertThat(result.getImageUrl()).isEqualTo(TEST_USER.getImageUrl());
        assertThat(result.getNickName()).isEqualTo(TEST_USER.getNickName());
        assertThat(result.getScore()).isEqualTo(TEST_USER.getScore());
        assertThat(result.getPushToken()).isEqualTo(TEST_USER.getPushToken());

        then(userRepository).should(times(1)).findById(any());
    }
}