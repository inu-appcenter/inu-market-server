package inu.market.chatroom.domain;

import inu.market.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c left join fetch c.buyer where c.id=:itemId order by c.id desc")
    List<ChatRoom> findWithBuyerByItemId(@Param("itemId") Long itemId);

    void deleteAllByItem(Item item);

    @Query("select c from ChatRoom c " +
            "left join fetch c.buyer " +
            "left join fetch c.seller " +
            "left join fetch c.item " +
            "where c.buyer.id=:userId or c.seller.id=:userId " +
            "order by c.id desc ")
    public List<ChatRoom> findWithItemAndBuyerAndSellerBySellerIdOrBuyerId(@Param("userId") Long userId);

    @Query("select c from ChatRoom c " +
            "left join fetch c.buyer " +
            "left join fetch c.seller " +
            "left join fetch c.item " +
            "where c.id=:roomId")
    Optional<ChatRoom> findWithItemAndBuyerAndSellerById(@Param("roomId") Long roomId);

}
