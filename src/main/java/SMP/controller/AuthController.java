package SMP.controller;

import SMP.dto.LoginRequest;
import SMP.dto.LoginResponse;
import SMP.dto.RegisterRequest;
import SMP.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

		LoginResponse response = authService.login(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register/student")
	public ResponseEntity<String> registerStudent(@RequestBody RegisterRequest request) {

		String response = authService.registerStudent(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register/teacher")
	public ResponseEntity<String> registerTeacher(@RequestBody RegisterRequest request) {

		String response = authService.registerTeacher(request);

		return ResponseEntity.ok(response);
	}
}