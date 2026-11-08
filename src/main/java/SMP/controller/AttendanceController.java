package SMP.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SMP.dto.AttendanceResponse;
import SMP.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

	private final AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostMapping("/mark")
	public AttendanceResponse markAttendance(@RequestParam String status, Authentication authentication) {

		return attendanceService.markAttendance(authentication.getName(), status);
	}

	@GetMapping("/my")
	public List<AttendanceResponse> getMyAttendance(Authentication authentication) {

		return attendanceService.getStudentAttendance(authentication.getName());
	}

	@GetMapping("/date")
	public List<AttendanceResponse> getAttendanceByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		return attendanceService.getAttendanceByDate(date);
	}
}