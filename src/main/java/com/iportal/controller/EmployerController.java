package com.iportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iportal.dto.common.ApiResponse;
import com.iportal.dto.employer.ListEmployerResponse;
import com.iportal.dto.employer.ListUsernameResponse;
import com.iportal.dto.employer.UpdateEmployerByAdminRequest;
import com.iportal.dto.employer.UpdateEmployerRequest;
import com.iportal.dto.employer.UpdateEmployerResponse;
import com.iportal.dto.user.CreateEUserRequest;
import com.iportal.service.EmployerService;
import com.iportal.service.SecurityService;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {
	@Autowired
	private SecurityService securityService;

	@Autowired
	private EmployerService employerService;

	// Tạo nhà tuyển dụng
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<String>> createEUser(@RequestBody @Validated CreateEUserRequest request) {
		if (!securityService.hasRole("MANAGER") && !securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", "NO"));
		}
		employerService.createEUser(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Tạo nhà tuyển dụng thành công", "OK"));
	}

	// Xem danh sách toàn bộ nhà tuyển dụng
	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<List<ListEmployerResponse>>> getAllEmployer() {
		if (!securityService.hasRole("MANAGER") && !securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListEmployerResponse> response = employerService.getAllEmployer();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xem danh sách toàn bộ username nhà tuyển dụng
	@GetMapping("/get-all-username")
	public ResponseEntity<ApiResponse<List<ListUsernameResponse>>> getAllUsername() {
		if (!securityService.hasRole("MANAGER") && !securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListUsernameResponse> response = employerService.getAllUsername();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Lấy thông tin nhà tuyển dụng
	@GetMapping("/get")
	public ResponseEntity<ApiResponse<UpdateEmployerResponse>> getEmployerProfile() {
		if (securityService.hasRole("STUDENT")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateEmployerResponse response = employerService.getEmployerProfile();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Lấy thông tin nhà tuyển dụng
	@GetMapping("/get-by-admin")
	public ResponseEntity<ApiResponse<UpdateEmployerResponse>> getEmployerProfileByAdmin(@RequestParam String username) {
		if (!securityService.hasRole("ADMIN") && !securityService.hasRole("MANAGER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateEmployerResponse response = employerService.getEmployerProfileByAdmin(username);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Cập nhật thông tin nhà tuyển dụng (Cái này cần chỉnh sao cho quá trình cập
	// nhật cần thông qua admin hoặc manager)
	@PatchMapping("/update")
	public ResponseEntity<ApiResponse<UpdateEmployerResponse>> updateEmployer(
			@RequestBody @Validated UpdateEmployerRequest request) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateEmployerResponse response = employerService.updateEmployer(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật nhà tuyển dụng thành công", response));
	}

	@PatchMapping("/update/{username}")
	public ResponseEntity<ApiResponse<UpdateEmployerResponse>> updateEmployer(@PathVariable String username,
			@RequestBody @Validated UpdateEmployerByAdminRequest request) {
		if (securityService.hasRole("ADMIN") && securityService.hasRole("MANAGER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateEmployerResponse response = employerService.updateEmployerByAdmin(username, request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật nhà tuyển dụng thành công", response));
	}
}
