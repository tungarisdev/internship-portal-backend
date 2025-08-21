package com.iportal.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iportal.entity.StudentEmployer;

import jakarta.transaction.Transactional;

public interface StudentEmployerRepository extends JpaRepository<StudentEmployer, Integer> {

	@Query("SELECT EXISTS (SELECT 1 FROM StudentEmployer se WHERE se.student.id = :studentId AND se.employer.id = :employerId AND (se.status = 'APPROVE' OR se.status = 'PENDING'))")
	boolean existsByStudentIdAndEmployerIdWithStatus(@Param("studentId") int studentId,
			@Param("employerId") int employerId);
	
	@Query("SELECT EXISTS (SELECT 1 FROM StudentEmployer se WHERE se.student.id = :studentId AND se.status = 'APPROVE')")
	boolean existsByStudentIdWithApproveStatus(@Param("studentId") int studentId);
	
	@Query("SELECT se FROM StudentEmployer se WHERE se.student.id = :studentId AND se.employer.id = :employerId AND se.status = 'PENDING'")
	Optional<StudentEmployer> findByStudentIdAndEmployerIdWithStatus(@Param("studentId") int studentId, @Param("employerId") int employerId);
	
	@Query("SELECT se FROM StudentEmployer se WHERE se.student.id = :studentId AND se.employer.id = :employerId AND se.status = :status")
	Optional<StudentEmployer> findApplyedByStudentIdAndEmployerIdAndStatus(@Param("studentId") int studentId, @Param("employerId") int employerId, @Param("status") String status);
	
	@Query("SELECT se FROM StudentEmployer se WHERE se.student.id = :studentId AND se.employer.id = :employerId AND se.status = 'APPROVE'")
	Optional<StudentEmployer> findByStudentIdAndEmployerIdWithStatus2(@Param("studentId") int studentId, @Param("employerId") int employerId);

	List<StudentEmployer> findByEmployerIdAndStatus(int employerId, String status);
	
	List<StudentEmployer> findByStudentId(int studentId);
	
	@Query("SELECT se.reply FROM StudentEmployer se WHERE se.student.id = :studentId AND se.employer.id = :employerId AND se.status = :status AND se.timeUpdate = :timeUpdate")
    String getReply(
        @Param("studentId") int studentId,
        @Param("employerId") int employerId,
        @Param("status") String status,
        @Param("timeUpdate") LocalDateTime timeUpdate
    );
	
	@Modifying
    @Transactional
    @Query("DELETE FROM StudentEmployer se WHERE se.student.id = :studentId AND se.employer.id = :employerId AND se.status = :status")
    void cancelApply(int studentId, int employerId, String status);
}
