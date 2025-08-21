package com.iportal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iportal.dto.auth.LoginRequest;
import com.iportal.dto.auth.LoginResponse;
import com.iportal.dto.common.ApiResponse;
import com.iportal.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	
	// API đăng nhập
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
		LoginResponse response = authService.login(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Đăng nhập thành công", response));
	}
	
	// API đăng xuất
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<LoginResponse>> logout(@RequestParam String refreshToken) {
		authService.logout(refreshToken);
		return ResponseEntity.ok(new ApiResponse<>(true, "Đăng xuất thành công", null));
	}
	
	// API gọi refresh token
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
		LoginResponse response = authService.refreshToken(refreshToken);
	    return ResponseEntity.ok(new ApiResponse<>(true, "Refresh access token success", response));
	}
}
