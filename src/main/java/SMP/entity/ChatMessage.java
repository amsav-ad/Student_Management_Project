package SMP.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chatroom_id", nullable = false)
	private ChatRoom chatRoom;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	private String message;

	private LocalDateTime sentAt;

	public ChatMessage() {
	}

	public ChatMessage(ChatRoom chatRoom, User sender, String message, LocalDateTime sentAt) {

		this.chatRoom = chatRoom;
		this.sender = sender;
		this.message = message;
		this.sentAt = sentAt;
	}

	public Long getId() {
		return id;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public User getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}
}