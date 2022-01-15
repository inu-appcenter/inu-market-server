package inu.market.chatroom.domain;

import inu.market.item.domain.Item;
import inu.market.common.BaseEntity;
import inu.market.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    private Boolean sellerStatus;

    private Boolean buyerStatus;

    public static ChatRoom createChatRoom(Item item, User buyer, User seller) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.item = item;
        chatRoom.buyer = buyer;
        chatRoom.seller = seller;
        chatRoom.sellerStatus = true;
        chatRoom.buyerStatus = true;
        return chatRoom;
    }

}
