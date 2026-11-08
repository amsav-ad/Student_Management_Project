package SMP.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SMP.dto.ChatRoomRequest;
import SMP.dto.ChatRoomResponse;
import SMP.service.ChatRoomService;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	public ChatRoomController(ChatRoomService chatRoomService) {
		this.chatRoomService = chatRoomService;
	}

	@PostMapping
	public ChatRoomResponse createChatRoom(@RequestBody ChatRoomRequest request, Authentication authentication) {

		return chatRoomService.createChatRoom(request, authentication.getName());
	}

	@GetMapping("/teacher/my")
	public List<ChatRoomResponse> getTeacherChatRooms(Authentication authentication) {

		return chatRoomService.getTeacherChatRooms(authentication.getName());
	}

	@GetMapping("/student/my")
	public List<ChatRoomResponse> getStudentChatRooms(Authentication authentication) {

		return chatRoomService.getStudentChatRooms(authentication.getName());
	}

	@PutMapping("/{chatRoomId}/students/{studentId}")
	public ChatRoomResponse addStudentToChatRoom(@PathVariable Long chatRoomId, @PathVariable Long studentId,
			Authentication authentication) {

		return chatRoomService.addStudentToChatRoom(chatRoomId, studentId, authentication.getName());
	}
}