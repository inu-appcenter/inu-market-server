package inu.market.security.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static inu.market.CommonFixture.TEST_AUTHORIZATION;
import static inu.market.CommonFixture.TEST_EXPIRATION_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("토큰을 생성한다.")
    void createToken() {
        // given
        Long userId = 1L;

        // when
        String result = jwtUtil.createToken(userId);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("토큰에서 회원 ID 를 얻는다.")
    void getSubject() {
        // given
        String jwtToken = jwtUtil.createToken(1L);

        // when
        String result = jwtUtil.getSubject(jwtToken);

        // then
        assertThat(result).isEqualTo("1");
    }

    @Test
    @DisplayName("유효한 토큰인지 확인한다.")
    void isValidToken() {
        // given
        String jwtToken = jwtUtil.createToken(1L);

        // when
        boolean result = jwtUtil.isValidToken(jwtToken);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 토큰인지 확인한다.")
    void isNotValidToken() {
        // when
        boolean result = jwtUtil.isValidToken(TEST_AUTHORIZATION);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효기간이 지난 토큰인지 확인한다.")
    void isExpirationToken() {
        // when
        boolean result = jwtUtil.isValidToken(TEST_EXPIRATION_TOKEN);

        // then
        assertThat(result).isFalse();
    }


}