package com.iportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iportal.dto.common.ApiResponse;
import com.iportal.dto.student.ListStudentResponse;
import com.iportal.dto.student.ListUsernameResponse;
import com.iportal.dto.student.UpdateStudentByAdminRequest;
import com.iportal.dto.student.UpdateStudentRequest;
import com.iportal.dto.student.UpdateStudentResponse;
import com.iportal.dto.user.CreateSUserRequest;
import com.iportal.service.SecurityService;
import com.iportal.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	@Autowired
	private SecurityService securityService;

	@Autowired
	private StudentService studentService;

	// Tạo sinh viên
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<String>> createSUser(@RequestBody @Validated CreateSUserRequest request) {
		if (!securityService.hasRole("MANAGER") && !securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", "NO"));
		}
		studentService.createSUser(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Tạo sinh viên thành công", "OK"));
	}

	// Lấy toàn bộ danh sách sinh viên
	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<List<ListStudentResponse>>> getAllStudent() {
		if (!securityService.hasRole("MANAGER") && !securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListStudentResponse> response = studentService.getAllStudent();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Lấy toàn bộ danh sách username sinh viên
	@GetMapping("/get-all-username")
	public ResponseEntity<ApiResponse<List<ListUsernameResponse>>> getAllStudentUsername() {
		if (!securityService.hasRole("MANAGER") && !securityService.hasRole("ADMIN")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListUsernameResponse> response = studentService.getAllStudentUsername();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	@GetMapping("/get")
	public ResponseEntity<ApiResponse<UpdateStudentResponse>> getStudentProfile() {
		if (securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateStudentResponse response = studentService.getStudentProfile();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}
	
	@GetMapping("/get-by-admin")
	public ResponseEntity<ApiResponse<UpdateStudentResponse>> getStudentProfileByAdmin(@RequestParam String username) {
		if (!securityService.hasRole("ADMIN") && !securityService.hasRole("MANAGER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateStudentResponse response = studentService.getStudentProfileByAdmin(username);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Cập nhật thông tinh sinh viên
	@PatchMapping("/update")
	public ResponseEntity<ApiResponse<UpdateStudentResponse>> updateStudent(
			@RequestBody @Validated UpdateStudentRequest request) {
		if (securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateStudentResponse response = studentService.updateStudent(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật sinh viên thành công", response));
	}

	// Cập nhật thông tinh sinh viên
	@PatchMapping("/update/{username}")
	public ResponseEntity<ApiResponse<UpdateStudentResponse>> updateStudentByAdmin(@PathVariable String username,
			@RequestBody @Validated UpdateStudentByAdminRequest request) {
		if (securityService.hasRole("ADMIN") && securityService.hasRole("MANAGER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateStudentResponse response = studentService.updateStudentByAdmin(username, request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật sinh viên thành công", response));
	}
}
