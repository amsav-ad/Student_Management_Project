package SMP.dto;

public class StudentProfileResponse {

    private Long studentId;
    private String name;
    private String email;
    private String role;

    public StudentProfileResponse(
            Long studentId,
            String name,
            String email,
            String role) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getStudentId() {
        return studentId;
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