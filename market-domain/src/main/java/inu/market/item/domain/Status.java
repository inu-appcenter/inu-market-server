package inu.market.item.domain;

import inu.market.common.NotFoundException;

import java.util.Arrays;

public enum Status {

    SALE("판매중"),
    RESERVED("예약중"),
    COMPLETED("판매완료");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public static Status from(String status) {
        return Arrays.stream(values())
                .filter(v -> v.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("잘못된 상품 상태 타입 : %s", status)));
    }

    public String getStatus() {
        return status;
    }
}
