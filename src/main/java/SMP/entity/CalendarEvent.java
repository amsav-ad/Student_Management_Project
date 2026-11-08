package SMP.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "calendar_events")
public class CalendarEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	private LocalDate eventDate;

	private String eventType;

	@ManyToOne
	@JoinColumn(name = "teacher_id", nullable = false)
	private User teacher;

	public CalendarEvent() {
	}

	public CalendarEvent(String title, String description, LocalDate eventDate, String eventType, User teacher) {

		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.eventType = eventType;
		this.teacher = teacher;
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

	public User getTeacher() {
		return teacher;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
}