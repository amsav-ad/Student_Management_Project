package SMP.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SMP.dto.ChatMessageRequest;
import SMP.dto.ChatMessageResponse;
import SMP.service.ChatMessageService;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	public ChatMessageController(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	@PostMapping("/{chatRoomId}/messages")
	public ChatMessageResponse sendMessage(@PathVariable Long chatRoomId, @RequestBody ChatMessageRequest request,
			Authentication authentication) {

		return chatMessageService.sendMessage(chatRoomId, request, authentication.getName());
	}

	@GetMapping("/{chatRoomId}/messages")
	public List<ChatMessageResponse> getChatMessages(@PathVariable Long chatRoomId, Authentication authentication) {

		return chatMessageService.getChatMessages(chatRoomId, authentication.getName());
	}
}