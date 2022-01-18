package inu.market.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("회원을 생성한다.")
    void createUser() {
        // given
        int inuId = 201601757;
        Role role = Role.ROLE_USER;

        // when
        User result = User.createUser(inuId, role);

        // then
        assertThat(result.getInuId()).isEqualTo(inuId);
        assertThat(result.getRole()).isEqualTo(role);
        assertThat(result.getScore()).isEqualTo(0.0);
        assertThat(result.getImageUrl()).isNull();
        assertThat(result.getNickName()).isNull();
        assertThat(result.getPushToken()).isNull();
    }

    @Test
    @DisplayName("Push 토큰을 변경한다.")
    void changePushToken() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);

        // when
        user.changePushToken("PushToken");

        // then
        assertThat(user.getPushToken()).isEqualTo("PushToken");
    }

    @Test
    @DisplayName("프로필을 변경한다.")
    void changeProfile() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);

        // when
        user.changeProfile("황주환","이미지 URL");

        // then
        assertThat(user.getNickName()).isEqualTo("황주환");
        assertThat(user.getImageUrl()).isEqualTo("이미지 URL");
    }

    @Test
    @DisplayName("점수를 증가한다.")
    void increaseScore() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);

        // when
        user.increaseScore();

        // then
        assertThat(user.getScore()).isEqualTo(10.0);
    }
}