package SMP.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomResponse {

	private Long id;
	private String name;
	private String description;
	private LocalDateTime createdAt;
	private String teacherName;
	private List<String> studentNames;

	public ChatRoomResponse(Long id, String name, String description, LocalDateTime createdAt, String teacherName,
			List<String> studentNames) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.teacherName = teacherName;
		this.studentNames = studentNames;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public List<String> getStudentNames() {
		return studentNames;
	}
}