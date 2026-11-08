package SMP.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import SMP.dto.ChatRoomRequest;
import SMP.dto.ChatRoomResponse;
import SMP.entity.ChatRoom;
import SMP.entity.User;
import SMP.enums.Role;
import SMP.repository.ChatRoomRepository;
import SMP.repository.UserRepository;

@Service
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {

		this.chatRoomRepository = chatRoomRepository;
		this.userRepository = userRepository;
	}

	public ChatRoomResponse createChatRoom(ChatRoomRequest request, String email) {

		if (request.getName() == null || request.getName().isBlank()) {
			throw new RuntimeException("Chatroom name is required");
		}

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		if (teacher.getRole() != Role.TEACHER) {
			throw new RuntimeException("Only teachers can create chatrooms");
		}

		ChatRoom chatRoom = new ChatRoom(request.getName().trim(), request.getDescription(), LocalDateTime.now(),
				teacher);

		return convertToResponse(chatRoomRepository.save(chatRoom));
	}

	public List<ChatRoomResponse> getTeacherChatRooms(String email) {

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		return chatRoomRepository.findByTeacher_Id(teacher.getId()).stream().map(this::convertToResponse).toList();
	}

	public List<ChatRoomResponse> getStudentChatRooms(String email) {

		User student = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found"));

		return chatRoomRepository.findByStudents_Id(student.getId()).stream().map(this::convertToResponse).toList();
	}

	public ChatRoomResponse addStudentToChatRoom(Long chatRoomId, Long studentId, String teacherEmail) {

		User teacher = userRepository.findByEmail(teacherEmail)
				.orElseThrow(() -> new RuntimeException("Teacher not found"));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new RuntimeException("Chatroom not found"));

		if (!chatRoom.getTeacher().getId().equals(teacher.getId())) {
			throw new RuntimeException("You can only manage your own chatrooms");
		}

		User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

		if (student.getRole() != Role.STUDENT) {
			throw new RuntimeException("Only students can be assigned to chatrooms");
		}

		if (chatRoom.getStudents().stream().anyMatch(existing -> existing.getId().equals(studentId))) {
			throw new RuntimeException("Student is already in this chatroom");
		}

		chatRoom.getStudents().add(student);

		return convertToResponse(chatRoomRepository.save(chatRoom));
	}

	private ChatRoomResponse convertToResponse(ChatRoom chatRoom) {

		List<String> studentNames = chatRoom.getStudents().stream().map(User::getName).toList();

		return new ChatRoomResponse(chatRoom.getId(), chatRoom.getName(), chatRoom.getDescription(),
				chatRoom.getCreatedAt(), chatRoom.getTeacher().getName(), studentNames);
	}
}