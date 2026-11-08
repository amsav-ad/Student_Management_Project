package SMP.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatrooms")
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "teacher_id", nullable = false)
	private User teacher;

	@ManyToMany
	@JoinTable(name = "chatroom_members", joinColumns = @JoinColumn(name = "chatroom_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
	private Set<User> students = new HashSet<>();

	public ChatRoom() {
	}

	public ChatRoom(String name, String description, LocalDateTime createdAt, User teacher) {

		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.teacher = teacher;
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

	public User getTeacher() {
		return teacher;
	}

	public Set<User> getStudents() {
		return students;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public void setStudents(Set<User> students) {
		this.students = students;
	}
}