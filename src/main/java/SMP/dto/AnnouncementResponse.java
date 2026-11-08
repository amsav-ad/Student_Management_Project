package SMP.dto;

import java.time.LocalDateTime;

public class AnnouncementResponse {

	private Long id;
	private String title;
	private String message;
	private LocalDateTime createdAt;
	private String teacherName;

	public AnnouncementResponse(Long id, String title, String message, LocalDateTime createdAt, String teacherName) {

		this.id = id;
		this.title = title;
		this.message = message;
		this.createdAt = createdAt;
		this.teacherName = teacherName;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getTeacherName() {
		return teacherName;
	}
}