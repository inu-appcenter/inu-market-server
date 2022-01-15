package inu.market.item.domain;

public enum Status {

    SALE("판매중"),
    RESERVED("예약중"),
    COMPLETED("판매 완료");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
