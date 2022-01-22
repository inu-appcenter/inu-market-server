package inu.market.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotMatchExceptionTest {

    @Test
    @DisplayName("매치 X 예외를 생성한다.")
    void notMatchException() {
        // given
        String message = "메세지";

        // when
        NotMatchException result = new NotMatchException(message);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
    }

}