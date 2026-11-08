package SMP.service;

import SMP.dto.StudentProfileResponse;
import SMP.entity.User;
import SMP.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileService {

	private final UserRepository userRepository;

	public StudentProfileService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public StudentProfileResponse getStudentProfile(Authentication authentication) {

		String email = authentication.getName();

		User student = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found"));

		return new StudentProfileResponse(student.getId(), student.getName(), student.getEmail(),
				student.getRole().name());
	}
}