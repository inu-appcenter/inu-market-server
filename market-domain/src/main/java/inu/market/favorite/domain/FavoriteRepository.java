package inu.market.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select f from Favorite f left join fetch f.item where f.user.id=:userId and f.item=:itemId")
    Optional<Favorite> findWithItemByUserIdAndItemId(@Param("userId") Long userId,@Param("itemId") Long itemId);

}
