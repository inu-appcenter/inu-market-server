package inu.market.chatroom.domain;

import inu.market.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c left join fetch c.buyer where c.id=:itemId order by c.id desc")
    List<ChatRoom> findWithBuyerByItemId(@Param("itemId") Long itemId);

    void deleteAllByItem(Item item);
}
