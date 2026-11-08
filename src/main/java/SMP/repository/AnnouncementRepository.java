package SMP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SMP.entity.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	List<Announcement> findAllByOrderByCreatedAtDesc();

}