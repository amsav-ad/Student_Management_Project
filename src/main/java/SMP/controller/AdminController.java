package SMP.controller;

import SMP.entity.User;
import SMP.enums.Role;
import SMP.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final UserRepository userRepository;

	public AdminController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/teachers/pending")
	public ResponseEntity<List<Map<String, Object>>> getPendingTeachers() {

		List<Map<String, Object>> teachers = userRepository.findAll().stream()
				.filter(user -> user.getRole() == Role.TEACHER).filter(user -> !user.isActive())
				.map(user -> Map.<String, Object>of("id", user.getId(), "name", user.getName(), "email",
						user.getEmail(), "createdAt", user.getCreatedAt()))
				.toList();

		return ResponseEntity.ok(teachers);
	}

	@PutMapping("/teachers/{id}/approve")
	public ResponseEntity<String> approveTeacher(@PathVariable Long id) {

		User teacher = userRepository.findById(id).orElse(null);

		if (teacher == null) {
			return ResponseEntity.notFound().build();
		}

		if (teacher.getRole() != Role.TEACHER) {
			return ResponseEntity.badRequest().body("This user is not a teacher");
		}

		if (teacher.isActive()) {
			return ResponseEntity.badRequest().body("Teacher account is already approved");
		}

		teacher.setActive(true);

		userRepository.save(teacher);

		return ResponseEntity.ok("Teacher account approved successfully");
	}
}