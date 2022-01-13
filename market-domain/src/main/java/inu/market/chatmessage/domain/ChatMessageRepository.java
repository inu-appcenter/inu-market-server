package inu.market.chatmessage.domain;

import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
