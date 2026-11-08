package SMP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SMP.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findByChatRoom_IdOrderBySentAtAsc(Long chatRoomId);
}