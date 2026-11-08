package SMP.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import SMP.dto.TeacherProfileResponse;
import SMP.entity.User;
import SMP.enums.Role;
import SMP.repository.UserRepository;

@Service
public class TeacherProfileService {

	private final UserRepository userRepository;

	public TeacherProfileService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public TeacherProfileResponse getTeacherProfile(Authentication authentication) {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		if (user.getRole() != Role.TEACHER) {
			throw new RuntimeException("Access denied: Not a teacher");
		}

		return new TeacherProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
	}
}