package SMP.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import SMP.dto.AttendanceReportResponse;
import SMP.service.AttendanceReportService;

@RestController
@RequestMapping("/api/attendance/reports")
public class AttendanceReportController {

	private final AttendanceReportService attendanceReportService;

	public AttendanceReportController(AttendanceReportService attendanceReportService) {

		this.attendanceReportService = attendanceReportService;
	}

	@GetMapping("/student/{studentId}")
	public AttendanceReportResponse getStudentReport(@PathVariable Long studentId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return attendanceReportService.getStudentReport(studentId, startDate, endDate);
	}

	@GetMapping("/all")
	public List<AttendanceReportResponse> getAllStudentReports(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return attendanceReportService.getAllStudentReports(startDate, endDate);
	}
}