package inu.market.notification.domain;

public enum NotificationType {

    FAVORITE("찜"),
    TRADE("거래");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
