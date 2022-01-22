package inu.market.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    @DisplayName("찾기 X 예외를 생성한다.")
    void notFoundException() {
        // given
        String message = "메세지";

        // when
        NotFoundException result = new NotFoundException(message);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
    }
}