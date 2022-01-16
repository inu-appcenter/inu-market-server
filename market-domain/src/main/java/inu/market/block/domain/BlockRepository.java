package inu.market.block.domain;

import inu.market.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByUserAndTarget(User user, User target);
}
