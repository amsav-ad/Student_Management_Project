package SMP.dto;

public class AttendanceReportResponse {

	private Long studentId;
	private String studentName;
	private long totalDays;
	private long presentDays;
	private long absentDays;
	private double attendancePercentage;

	public AttendanceReportResponse(Long studentId, String studentName, long totalDays, long presentDays,
			long absentDays, double attendancePercentage) {

		this.studentId = studentId;
		this.studentName = studentName;
		this.totalDays = totalDays;
		this.presentDays = presentDays;
		this.absentDays = absentDays;
		this.attendancePercentage = attendancePercentage;
	}

	public Long getStudentId() {
		return studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public long getTotalDays() {
		return totalDays;
	}

	public long getPresentDays() {
		return presentDays;
	}

	public long getAbsentDays() {
		return absentDays;
	}

	public double getAttendancePercentage() {
		return attendancePercentage;
	}
}