package inu.market.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NetworkExceptionTest {

    @Test
    @DisplayName("네트워크 예외를 생성한다.")
    void networkException() {
        // given
        String message = "메세지";

        // when
        NetworkException result = new NetworkException(message);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
    }

}