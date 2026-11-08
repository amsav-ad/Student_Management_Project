package SMP.dto;

import java.time.LocalDate;

public class AttendanceResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private LocalDate date;
    private String status;

    public AttendanceResponse(Long id, Long studentId, String studentName,
            LocalDate date, String status) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}