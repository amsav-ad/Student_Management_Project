package SMP.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import SMP.dto.ChatMessageRequest;
import SMP.dto.ChatMessageResponse;
import SMP.entity.ChatMessage;
import SMP.entity.ChatRoom;
import SMP.entity.User;
import SMP.repository.ChatMessageRepository;
import SMP.repository.ChatRoomRepository;
import SMP.repository.UserRepository;

@Service
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository,
			UserRepository userRepository) {

		this.chatMessageRepository = chatMessageRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.userRepository = userRepository;
	}

	public ChatMessageResponse sendMessage(Long chatRoomId, ChatMessageRequest request, String email) {

		User sender = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new RuntimeException("Chatroom not found"));

		validateMembership(chatRoom, sender);

		if (request.getMessage() == null || request.getMessage().isBlank()) {
			throw new RuntimeException("Message cannot be empty");
		}

		ChatMessage chatMessage = new ChatMessage(chatRoom, sender, request.getMessage(), LocalDateTime.now());

		ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

		return convertToResponse(savedMessage);
	}

	public List<ChatMessageResponse> getChatMessages(Long chatRoomId, String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new RuntimeException("Chatroom not found"));

		validateMembership(chatRoom, user);

		return chatMessageRepository.findByChatRoom_IdOrderBySentAtAsc(chatRoomId).stream().map(this::convertToResponse)
				.toList();
	}

	private void validateMembership(ChatRoom chatRoom, User user) {

		boolean isTeacher = chatRoom.getTeacher().getId().equals(user.getId());

		boolean isStudent = chatRoom.getStudents().stream().anyMatch(student -> student.getId().equals(user.getId()));

		if (!isTeacher && !isStudent) {
			throw new RuntimeException("You are not a member of this chatroom");
		}
	}

	private ChatMessageResponse convertToResponse(ChatMessage chatMessage) {

		return new ChatMessageResponse(chatMessage.getId(), chatMessage.getChatRoom().getId(),
				chatMessage.getSender().getName(), chatMessage.getMessage(), chatMessage.getSentAt());
	}
}