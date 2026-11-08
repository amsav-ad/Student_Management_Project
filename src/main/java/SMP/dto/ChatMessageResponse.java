package SMP.dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {

	private Long id;
	private Long chatRoomId;
	private String senderName;
	private String message;
	private LocalDateTime sentAt;

	public ChatMessageResponse(Long id, Long chatRoomId, String senderName, String message, LocalDateTime sentAt) {

		this.id = id;
		this.chatRoomId = chatRoomId;
		this.senderName = senderName;
		this.message = message;
		this.sentAt = sentAt;
	}

	public Long getId() {
		return id;
	}

	public Long getChatRoomId() {
		return chatRoomId;
	}

	public String getSenderName() {
		return senderName;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}
}