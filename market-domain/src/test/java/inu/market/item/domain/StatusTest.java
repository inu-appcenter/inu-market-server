package inu.market.item.domain;

import inu.market.common.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    @DisplayName("상품 상태를 생성한다.")
    void from() {
        // given
        String status = "판매중";

        // when
        Status result = Status.from(status);

        // then
        assertThat(result).isEqualTo(Status.SALE);
    }

    @Test
    @DisplayName("상품 상태를 생성할때 존재하지 않으면 예외가 발생한다.")
    void fromNotFound() {
        // given
        String status = "몰라";

        // when
        assertThrows(NotFoundException.class, () -> Status.from(status));
    }

    @Test
    @DisplayName("상품 상태를 문자열로 얻는다.")
    void getStatus() {
        // when
        String result = Status.SALE.getStatus();

        // then
        assertThat(result).isEqualTo("판매중");
    }
}