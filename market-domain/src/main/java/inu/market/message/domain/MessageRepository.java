package inu.market.message.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findTopByRoomIdOrderByCreatedAtDesc(Long roomId);

    List<Message> findAllByRoomIdAndCreatedAtIsBeforeOrderByCreatedAtDesc(Long roomId, LocalDateTime createdAt, Pageable pageable);
}
