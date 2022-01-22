package inu.market.common;

public class NotExistException extends RuntimeException{

    public static final String IMAGE_NOT_EXIST = "이미지가 없습니다.";

    public NotExistException(String message) {
        super(message);
    }
}
