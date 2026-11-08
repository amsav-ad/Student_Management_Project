package SMP.service;

import SMP.dto.LoginRequest;
import SMP.dto.LoginResponse;
import SMP.dto.RegisterRequest;
import SMP.entity.User;
import SMP.enums.Role;
import SMP.repository.UserRepository;
import SMP.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
			JwtService jwtService, PasswordEncoder passwordEncoder) {

		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	public LoginResponse login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!user.isActive()) {
			throw new RuntimeException("Your account is waiting for administrator approval");
		}

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

		return new LoginResponse(token, user.getId(), user.getName(), user.getRole());
	}

	public String registerStudent(RegisterRequest request) {

		validateRegistration(request);

		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.STUDENT);
		user.setActive(true);

		userRepository.save(user);

		return "Student registration successful. You can now log in.";
	}

	public String registerTeacher(RegisterRequest request) {

		validateRegistration(request);

		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.TEACHER);
		user.setActive(false);

		userRepository.save(user);

		return "Teacher registration successful. Waiting for administrator approval.";
	}

	private void validateRegistration(RegisterRequest request) {

		if (request.getName() == null || request.getName().isBlank()) {
			throw new RuntimeException("Name is required");
		}

		if (request.getEmail() == null || request.getEmail().isBlank()) {
			throw new RuntimeException("Email is required");
		}

		if (request.getPassword() == null || request.getPassword().length() < 8) {
			throw new RuntimeException("Password must contain at least 8 characters");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email is already registered");
		}
	}
}