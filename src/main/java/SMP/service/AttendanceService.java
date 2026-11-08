package SMP.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import SMP.dto.AttendanceResponse;
import SMP.entity.Attendance;
import SMP.entity.User;
import SMP.enums.Role;
import SMP.repository.AttendanceRepository;
import SMP.repository.UserRepository;

@Service
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final UserRepository userRepository;

	public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository) {

		this.attendanceRepository = attendanceRepository;
		this.userRepository = userRepository;
	}

	public AttendanceResponse markAttendance(String email, String status) {

		if (status == null || (!status.equalsIgnoreCase("PRESENT") && !status.equalsIgnoreCase("ABSENT"))) {

			throw new RuntimeException("Attendance status must be PRESENT or ABSENT");
		}

		User student = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found"));

		if (student.getRole() != Role.STUDENT) {
			throw new RuntimeException("Only students can mark their attendance");
		}

		LocalDate today = LocalDate.now();

		if (attendanceRepository.findByStudent_IdAndDate(student.getId(), today).isPresent()) {

			throw new RuntimeException("Attendance already marked today");
		}

		Attendance attendance = new Attendance(student, today, status.toUpperCase());

		Attendance savedAttendance = attendanceRepository.save(attendance);

		return convertToResponse(savedAttendance);
	}

	public List<AttendanceResponse> getStudentAttendance(String email) {

		User student = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found"));

		return attendanceRepository.findByStudent_IdOrderByDateDesc(student.getId()).stream()
				.map(this::convertToResponse).toList();
	}

	public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {

		return attendanceRepository.findByDate(date).stream().map(this::convertToResponse).toList();
	}

	private AttendanceResponse convertToResponse(Attendance attendance) {

		return new AttendanceResponse(attendance.getId(), attendance.getStudent().getId(),
				attendance.getStudent().getName(), attendance.getDate(), attendance.getStatus());
	}
}