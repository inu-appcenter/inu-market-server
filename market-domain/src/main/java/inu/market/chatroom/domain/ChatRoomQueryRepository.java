package inu.market.chatroom.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.market.chatroom.domain.QChatRoom.chatRoom;
import static inu.market.item.domain.QItem.item;
import static inu.market.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChatRoom> findWithItemAndBuyerAndSellerBySellerIdOrBuyerId(Long userId) {
        return queryFactory
                .selectFrom(chatRoom)
                .leftJoin(chatRoom.buyer, user).fetchJoin()
                .leftJoin(chatRoom.seller, user).fetchJoin()
                .leftJoin(chatRoom.item, item).fetchJoin()
                .where(chatRoom.buyer.id.eq(userId).or(chatRoom.seller.id.eq(userId)))
                .fetch();
    }
}
