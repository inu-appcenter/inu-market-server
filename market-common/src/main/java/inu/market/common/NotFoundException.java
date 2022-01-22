package inu.market.common;

public class NotFoundException extends RuntimeException {

    public static final String USER_NOT_FOUND = "존재하지 않는 회원입니다.";
    public static final String BLOCK_NOT_FOUND = "존재하지 않는 차단입니다.";
    public static final String CATEGORY_NOT_FOUND = "존재하지 않는 카테고리입니다.";
    public static final String ITEM_NOT_FOUND = "존재하지 않는 상품입니다.";
    public static final String CHAT_ROOM_NOT_FOUND = "존재하지 않는 채팅방입니다.";
    public static final String FAVORITE_NOT_FOUND = "찜 목록에 존재하지 않습니다.";
    public static final String MAJOR_NOT_FOUND = "존재하지 않는 학과입니다.";
    public static final String NOTICE_NOT_FOUND = "존재하지 않는 공지사항입니다.";
    public static final String NOTIFICATION_NOT_FOUND = "존재하지 않는 알림입니다.";


    public NotFoundException(String message) {
        super(message);
    }
}
