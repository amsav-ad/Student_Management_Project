package SMP.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import SMP.dto.CalendarEventRequest;
import SMP.dto.CalendarEventResponse;
import SMP.entity.CalendarEvent;
import SMP.entity.User;
import SMP.repository.CalendarEventRepository;
import SMP.repository.UserRepository;

@Service
public class CalendarEventService {

	private final CalendarEventRepository calendarEventRepository;
	private final UserRepository userRepository;

	public CalendarEventService(CalendarEventRepository calendarEventRepository, UserRepository userRepository) {

		this.calendarEventRepository = calendarEventRepository;
		this.userRepository = userRepository;
	}

	public CalendarEventResponse createEvent(CalendarEventRequest request, String email) {

		validateEvent(request);

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		CalendarEvent event = new CalendarEvent(request.getTitle().trim(), request.getDescription(),
				request.getEventDate(), request.getEventType().trim(), teacher);

		return convertToResponse(calendarEventRepository.save(event));
	}

	public List<CalendarEventResponse> getAllEvents() {

		return calendarEventRepository.findAllByOrderByEventDateAsc().stream().map(this::convertToResponse).toList();
	}

	public List<CalendarEventResponse> getEventsBetweenDates(LocalDate startDate, LocalDate endDate) {

		validateDateRange(startDate, endDate);

		return calendarEventRepository.findByEventDateBetweenOrderByEventDateAsc(startDate, endDate).stream()
				.map(this::convertToResponse).toList();
	}

	public List<CalendarEventResponse> getEventsByType(String eventType) {

		if (eventType == null || eventType.isBlank()) {
			throw new RuntimeException("Event type is required");
		}

		return calendarEventRepository.findByEventTypeOrderByEventDateAsc(eventType.trim()).stream()
				.map(this::convertToResponse).toList();
	}

	public CalendarEventResponse updateEvent(Long id, CalendarEventRequest request, String email) {

		validateEvent(request);

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		CalendarEvent event = calendarEventRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Event not found"));

		if (!event.getTeacher().getId().equals(teacher.getId())) {
			throw new RuntimeException("You can only update your own events");
		}

		event.setTitle(request.getTitle().trim());
		event.setDescription(request.getDescription());
		event.setEventDate(request.getEventDate());
		event.setEventType(request.getEventType().trim());

		return convertToResponse(calendarEventRepository.save(event));
	}

	public void deleteEvent(Long id, String email) {

		User teacher = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Teacher not found"));

		CalendarEvent event = calendarEventRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Event not found"));

		if (!event.getTeacher().getId().equals(teacher.getId())) {
			throw new RuntimeException("You can only delete your own events");
		}

		calendarEventRepository.delete(event);
	}

	private void validateEvent(CalendarEventRequest request) {

		if (request.getTitle() == null || request.getTitle().isBlank()) {
			throw new RuntimeException("Event title is required");
		}

		if (request.getEventDate() == null) {
			throw new RuntimeException("Event date is required");
		}

		if (request.getEventType() == null || request.getEventType().isBlank()) {
			throw new RuntimeException("Event type is required");
		}
	}

	private void validateDateRange(LocalDate startDate, LocalDate endDate) {

		if (startDate == null || endDate == null) {
			throw new RuntimeException("Start date and end date are required");
		}

		if (startDate.isAfter(endDate)) {
			throw new RuntimeException("Start date cannot be after end date");
		}
	}

	private CalendarEventResponse convertToResponse(CalendarEvent event) {

		return new CalendarEventResponse(event.getId(), event.getTitle(), event.getDescription(), event.getEventDate(),
				event.getEventType(), event.getTeacher().getName());
	}
}