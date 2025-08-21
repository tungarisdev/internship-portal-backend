package com.iportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iportal.dto.employer.ListEmployerResponse;
import com.iportal.dto.employer.ListUsernameResponse;
import com.iportal.entity.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Integer>{

	Optional<Employer> findByUser_id(int id);
	
	Optional<Employer> findByUser_username(String username);
	
	@Query("SELECT new com.iportal.dto.employer.ListEmployerResponse(e.user.username, e.companyName, e.address, e.email, e.phoneNumber, e.website, e.user.status) "
			+ "FROM Employer e JOIN e.user u")
	List<ListEmployerResponse> getAllEmployers();
	
	@Query("SELECT new com.iportal.dto.employer.ListUsernameResponse(e.user.username) "
			+ "FROM Employer e JOIN e.user u ")
	List<ListUsernameResponse> getAllUsername();
}
