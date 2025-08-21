package com.iportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iportal.entity.Recruitment;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer>{
	Recruitment findByIdAndRecruitmentStatus(int id, String recruitmentStatus);
	
	List<Recruitment> findByEmployer_idAndRecruitmentStatus(int id, String recruitmentStatus);

	List<Recruitment> findAllByStatusAndRecruitmentStatus(String status, String recruitmentStatus);
	
	List<Recruitment> findAllByRecruitmentStatus(String recruitmentStatus);
	
	@Query("SELECT r FROM Recruitment r WHERE r.id = :recruitmentId AND r.status = 'OPEN' AND r.recruitmentStatus = 'ACCEPTED'")
	Optional<Recruitment> findByRecruitmentAndStatusAndRecruitmentStatus(@Param("recruitmentId") int recruitmentId);
	
	@Query("SELECT r FROM Recruitment r WHERE r.id = :recruitmentId")
	Optional<Recruitment> findByRecruitment(@Param("recruitmentId") int recruitmentId);

	Optional<Recruitment> findAllByRecruitmentStatusAndEmployer_id(String recruitmentStatus, int employerId);
}
