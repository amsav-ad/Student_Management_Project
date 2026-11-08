package SMP.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import SMP.dto.AttendanceReportResponse;
import SMP.entity.Attendance;
import SMP.entity.User;
import SMP.enums.Role;
import SMP.repository.AttendanceRepository;
import SMP.repository.UserRepository;

@Service
public class AttendanceReportService {

	private final AttendanceRepository attendanceRepository;
	private final UserRepository userRepository;

	public AttendanceReportService(AttendanceRepository attendanceRepository, UserRepository userRepository) {

		this.attendanceRepository = attendanceRepository;
		this.userRepository = userRepository;
	}

	public AttendanceReportResponse getStudentReport(Long studentId, LocalDate startDate, LocalDate endDate) {

		validateDateRange(startDate, endDate);

		User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

		if (student.getRole() != Role.STUDENT) {
			throw new RuntimeException("Attendance reports are available only for students");
		}

		List<Attendance> records = attendanceRepository.findByStudent_IdAndDateBetweenOrderByDateDesc(studentId,
				startDate, endDate);

		long totalDays = records.size();

		long presentDays = records.stream().filter(record -> "PRESENT".equalsIgnoreCase(record.getStatus())).count();

		long absentDays = records.stream().filter(record -> "ABSENT".equalsIgnoreCase(record.getStatus())).count();

		double percentage = totalDays == 0 ? 0.0 : (presentDays * 100.0) / totalDays;

		return new AttendanceReportResponse(student.getId(), student.getName(), totalDays, presentDays, absentDays,
				Math.round(percentage * 100.0) / 100.0);
	}

	public List<AttendanceReportResponse> getAllStudentReports(LocalDate startDate, LocalDate endDate) {

		validateDateRange(startDate, endDate);

		List<Attendance> records = attendanceRepository.findByDateBetweenOrderByDateAsc(startDate, endDate);

		return records.stream().map(Attendance::getStudent).filter(student -> student.getRole() == Role.STUDENT)
				.distinct().map(student -> getStudentReport(student.getId(), startDate, endDate)).toList();
	}

	private void validateDateRange(LocalDate startDate, LocalDate endDate) {

		if (startDate == null || endDate == null) {
			throw new RuntimeException("Start date and end date are required");
		}

		if (startDate.isAfter(endDate)) {
			throw new RuntimeException("Start date cannot be after end date");
		}
	}
}