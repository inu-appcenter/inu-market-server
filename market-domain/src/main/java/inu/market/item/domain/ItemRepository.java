package inu.market.item.domain;

import inu.market.item.domain.Item;
import org.aspectj.weaver.IEclipseSourceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository  extends JpaRepository<Item, Long> {

    @Query("select i from Item i left join fetch i.seller where i.id=:itemId")
    Optional<Item> findWithSellerById(@Param("itemId") Long itemId);
}
