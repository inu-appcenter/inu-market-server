package inu.market.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionResponseTest {

    @Test
    @DisplayName("예외 응답를 생성한다.")
    void from() {
        // given
        String message = "메세지";

        // when
        ExceptionResponse result = ExceptionResponse.from(message);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
        assertThat(result.getAttributes()).isNull();
    }

    @Test
    @DisplayName("예외 응답을 생성한다.")
    void testFrom() {
        // given
        String message = "메세지";
        Map<String, String> errors = new HashMap<>();

        // when
        ExceptionResponse result = ExceptionResponse.from(message,errors);

        // then
        assertThat(result.getMessage()).isEqualTo(message);
        assertThat(result.getAttributes()).isEqualTo(errors);
    }
}