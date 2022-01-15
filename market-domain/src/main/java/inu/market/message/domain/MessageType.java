package inu.market.message.domain;

public enum MessageType {

    TEXT("text"),
    IMAGE("image");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
