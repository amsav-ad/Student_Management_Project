package SMP.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SMP.dto.AnnouncementRequest;
import SMP.dto.AnnouncementResponse;
import SMP.service.AnnouncementService;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

	private final AnnouncementService announcementService;

	public AnnouncementController(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	@PostMapping
	public AnnouncementResponse createAnnouncement(@RequestBody AnnouncementRequest request,
			Authentication authentication) {

		return announcementService.createAnnouncement(request, authentication.getName());
	}

	@GetMapping
	public List<AnnouncementResponse> getAllAnnouncements() {
		return announcementService.getAllAnnouncements();
	}

	@GetMapping("/{id}")
	public AnnouncementResponse getAnnouncementById(@PathVariable Long id) {

		return announcementService.getAnnouncementById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteAnnouncement(@PathVariable Long id, Authentication authentication) {

		announcementService.deleteAnnouncement(id, authentication.getName());
	}
}