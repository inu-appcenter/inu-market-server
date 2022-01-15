package inu.market.trade.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("select t from Trade t left join fetch t.item where t.buyer.id=:buyerId order by t.id desc")
    List<Trade> findWithItemByBuyerId(@Param("buyerId") Long buyerId);
}
