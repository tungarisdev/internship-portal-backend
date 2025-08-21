package com.iportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iportal.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	Optional<User> findByUsername(String username);
	
	List<User> findByRole_id(Long role);
}
