package com.iportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iportal.entity.Token;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long>{
	void deleteByUsername(String username);
	Optional<Token> findByToken(String token);
	
	@Modifying
	@Query("DELETE FROM Token t WHERE t.username = :username")
	void deleteAllByUsername(@Param("username") String username);
}
