package SMP.dto;

import java.time.LocalDate;

public class CalendarEventResponse {

	private Long id;
	private String title;
	private String description;
	private LocalDate eventDate;
	private String eventType;
	private String teacherName;

	public CalendarEventResponse(Long id, String title, String description, LocalDate eventDate, String eventType,
			String teacherName) {

		this.id = id;
		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.eventType = eventType;
		this.teacherName = teacherName;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public String getEventType() {
		return eventType;
	}

	public String getTeacherName() {
		return teacherName;
	}
}