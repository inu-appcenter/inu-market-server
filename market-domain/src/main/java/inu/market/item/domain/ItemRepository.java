package inu.market.item.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository  extends JpaRepository<Item, Long> {

    @Query("select i from Item i left join fetch i.seller where i.id=:itemId")
    Optional<Item> findWithSellerById(@Param("itemId") Long itemId);

    @Query("select distinct i from Item i left join fetch i.itemImages where i.id=:itemId")
    Optional<Item> findWithItemImagesById(@Param("itemId") Long itemId);

    @Query("select distinct i from Item i " +
            "left join fetch i.seller " +
            "left join fetch i.category " +
            "left join fetch i.major " +
            "left join fetch i.itemImages " +
            "where i.id=:itemId")
    Optional<Item> findWithSellerAndItemImagesAndCategoryAndMajorById(@Param("itemId") Long itemId);
}

