package inu.market.block.domain;

import inu.market.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByUserAndTarget(User user, User target);

    @Query("select b from Block b left join fetch b.target where b.id=:userId order by b.id desc")
    List<Block> findByUserId(@Param("userId") Long userId);
}
