package inu.market.message.domain;

import inu.market.common.NotFoundException;

import java.util.Arrays;

public enum MessageType {

    TEXT("문자"),
    IMAGE("이미지");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public static MessageType from(String messageType) {
        return Arrays.stream(values())
                .filter(v -> v.type.equals(messageType))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("잘못된 메세지 타입 : &s", messageType)));
    }

    public String getType() {
        return type;
    }
}
