package SMP.dto;

public class TeacherProfileResponse {

	private Long teacherId;
	private String name;
	private String email;
	private String role;

	public TeacherProfileResponse(Long teacherId, String name, String email, String role) {
		this.teacherId = teacherId;
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}
}