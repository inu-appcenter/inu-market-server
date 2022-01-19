package inu.market.trade.domain;

import inu.market.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    void deleteAllByItem(Item item);
}
