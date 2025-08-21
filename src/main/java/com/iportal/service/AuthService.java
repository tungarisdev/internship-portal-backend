package com.iportal.service;

import com.iportal.dto.auth.LoginRequest;
import com.iportal.dto.auth.LoginResponse;

public interface AuthService {
	LoginResponse login(LoginRequest request);
	
	void logout(String refreshToken);

	LoginResponse refreshToken(String refreshToken);
}
