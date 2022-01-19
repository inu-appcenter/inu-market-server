package inu.market.item.domain;

public enum Status {

    SALE("SALE"),
    RESERVED("RESERVE"),
    COMPLETED("COMPLETED");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
