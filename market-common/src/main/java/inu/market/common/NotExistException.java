package inu.market.common;

public class NotExistException extends RuntimeException{

    public NotExistException(String message) {
        super(message);
    }
}
