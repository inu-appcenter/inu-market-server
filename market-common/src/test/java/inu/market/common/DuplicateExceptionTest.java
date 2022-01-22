package inu.market.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateExceptionTest {

    @Test
    @DisplayName("중복 예외를 생성한다.")
    void duplicateException() {
        // given
        String message = "메세지";

        // when
        DuplicateException result = new DuplicateException(message);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
    }

}