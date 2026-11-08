package SMP.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SMP.entity.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

	List<CalendarEvent> findAllByOrderByEventDateAsc();

	List<CalendarEvent> findByEventDateBetweenOrderByEventDateAsc(LocalDate startDate, LocalDate endDate);

	List<CalendarEvent> findByEventTypeOrderByEventDateAsc(String eventType);
}