package com.iportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iportal.dto.common.ApiResponse;
import com.iportal.dto.user.ChangePasswordRequest;
import com.iportal.service.SecurityService;
import com.iportal.service.UserService;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	// Cấm tài khoản (API này code chơi chơi nhằm mục đích phân biệt admin và
	// manager
	@PatchMapping("/ban-account")
	public ResponseEntity<ApiResponse<String>> banAccount(@RequestParam String username) {
		if (!securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		String ahihi = "admin";
		if (username.equals(ahihi)) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Không thể vô hiệu tài khoản này", null));
		}
		userService.banAccount(username);
		return ResponseEntity.ok(new ApiResponse<>(true, "Vô hiệu hóa tài khoản thành công", "OK"));
	}
	
	@PatchMapping("/unban-account")
	public ResponseEntity<ApiResponse<String>> unbanAccount(@RequestParam String username) {
		if (!securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		String ahihi = "admin";
		if (username.equals(ahihi)) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Không thể vô hiệu tài khoản này", null));
		}
		userService.unbanAccount(username);
		return ResponseEntity.ok(new ApiResponse<>(true, "Hủy vô hiệu hóa tài khoản thành công", "OK"));
	}

	// Thay đổi mật khẩu
	@PatchMapping("/change-password")
	public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Validated ChangePasswordRequest request) {
		String username = securityService.getCurrentUsername();
		boolean isChange = userService.changePassword(username, request);
		if (isChange) {
			return ResponseEntity.ok(new ApiResponse<>(true, "Thay đổi mật khẩu thành công", "Change"));
		} else {
			return ResponseEntity.ok(new ApiResponse<>(false, "Thay đổi mật khẩu thất bại", "Unchange"));
		}
	}
}
