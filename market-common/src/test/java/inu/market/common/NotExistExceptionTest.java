package inu.market.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotExistExceptionTest {

    @Test
    @DisplayName("존재 X 예외를 생성한다.")
    void notExistException() {
        // given
        String message = "메세지";

        // when
        NotExistException result = new NotExistException(message);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
    }

}