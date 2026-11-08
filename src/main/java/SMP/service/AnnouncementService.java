package SMP.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import SMP.dto.AnnouncementRequest;
import SMP.dto.AnnouncementResponse;
import SMP.entity.Announcement;
import SMP.entity.User;
import SMP.repository.AnnouncementRepository;
import SMP.repository.UserRepository;

@Service
public class AnnouncementService {

	private final AnnouncementRepository announcementRepository;
	private final UserRepository userRepository;

	public AnnouncementService(AnnouncementRepository announcementRepository, UserRepository userRepository) {

		this.announcementRepository = announcementRepository;
		this.userRepository = userRepository;
	}

	public AnnouncementResponse createAnnouncement(AnnouncementRequest request, String email) {

		if (request.getTitle() == null || request.getTitle().isBlank()) {
			throw new RuntimeException("Announcement title is required");
		}

		if (request.getMessage() == null || request.getMessage().isBlank()) {
			throw new RuntimeException("Announcement message is required");
		}

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		Announcement announcement = new Announcement(request.getTitle().trim(), request.getMessage().trim(),
				LocalDateTime.now(), teacher);

		return convertToResponse(announcementRepository.save(announcement));
	}

	public List<AnnouncementResponse> getAllAnnouncements() {

		return announcementRepository.findAllByOrderByCreatedAtDesc().stream().map(this::convertToResponse).toList();
	}

	public AnnouncementResponse getAnnouncementById(Long id) {

		Announcement announcement = announcementRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Announcement not found"));

		return convertToResponse(announcement);
	}

	public void deleteAnnouncement(Long id, String email) {

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		Announcement announcement = announcementRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Announcement not found"));

		if (!announcement.getTeacher().getId().equals(teacher.getId())) {
			throw new RuntimeException("You can only delete your own announcements");
		}

		announcementRepository.delete(announcement);
	}

	private AnnouncementResponse convertToResponse(Announcement announcement) {

		return new AnnouncementResponse(announcement.getId(), announcement.getTitle(), announcement.getMessage(),
				announcement.getCreatedAt(), announcement.getTeacher().getName());
	}
}