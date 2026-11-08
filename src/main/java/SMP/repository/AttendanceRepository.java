package SMP.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import SMP.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	Optional<Attendance> findByStudent_IdAndDate(Long studentId, LocalDate date);

	List<Attendance> findByDate(LocalDate date);

	List<Attendance> findByStudent_IdOrderByDateDesc(Long studentId);

	List<Attendance> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);

	List<Attendance> findByStudent_IdAndDateBetweenOrderByDateDesc(Long studentId, LocalDate startDate,
			LocalDate endDate);
}