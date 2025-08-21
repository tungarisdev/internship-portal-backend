package com.iportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iportal.dto.student.ListStudentResponse;
import com.iportal.dto.student.ListUsernameResponse;
import com.iportal.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
	Optional<Student> findByUser_id(Integer userId);
	
	Optional<Student> findByUser_username(String username);

	@Query("SELECT new com.iportal.dto.student.ListStudentResponse(" +
		       "s.id, u.username, s.fullName, s.dob, s.classRoom, s.major, " +
		       "s.yearOfStudy, s.address, s.email, s.phoneNumber, u.status) " +
		       "FROM Student s JOIN s.user u")
		List<ListStudentResponse> getAllStudents();
	
	@Query("SELECT new com.iportal.dto.student.ListUsernameResponse(s.user.username) "
			+ "FROM Student s JOIN s.user u ")
	List<ListUsernameResponse> getAllUsername();
}
