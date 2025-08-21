package com.iportal.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String token;
	private Date expiration;
	private boolean isUsed;
    private boolean isRevoked;
	
	public Token(String username, String token, Date expiration, boolean isUsed, boolean isRevoked) {
		this.username = username;
		this.token = token;
		this.expiration = expiration;
		this.isUsed = isRevoked;
		this.isRevoked = isRevoked;
	}
}
