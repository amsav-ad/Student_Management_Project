package SMP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SMP.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findByTeacher_Id(Long teacherId);

	List<ChatRoom> findByStudents_Id(Long studentId);

	List<ChatRoom> findAllByOrderByCreatedAtDesc();
}