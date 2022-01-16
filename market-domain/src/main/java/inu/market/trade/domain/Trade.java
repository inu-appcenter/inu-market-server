package inu.market.trade.domain;

import inu.market.common.BaseEntity;
import inu.market.item.domain.Item;
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
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    public static Trade createTrade(Item item, User buyer){
        Trade trade = new Trade();
        trade.item = item;
        trade.buyer = buyer;
        buyer.increaseScore();
        item.getSeller().increaseScore();
        return trade;
    }

}
