package inu.market.notification.domain;

public enum NotificationType {

    FAVORITE("FAVORITE"),
    TRADE("TRADE");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
