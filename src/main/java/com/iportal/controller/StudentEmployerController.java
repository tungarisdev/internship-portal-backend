package com.iportal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iportal.dto.common.ApiResponse;
import com.iportal.dto.student_employer.ApplyInfoResponse;
import com.iportal.dto.student_employer.ApplyRequest;
import com.iportal.dto.student_employer.ListApplyedResponse;
import com.iportal.dto.student_employer.ListApprovedResponse;
import com.iportal.service.SecurityService;
import com.iportal.service.StudentEmployerService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/student-employer")
public class StudentEmployerController {
	@Autowired
	private SecurityService securityService;

	@Autowired
	private StudentEmployerService studentEmployerService;

	// Nộp CV
	@PostMapping("/apply")
	public ResponseEntity<ApiResponse<String>> request(@RequestParam int recruitmentId, @RequestParam int employerId,
			@RequestBody @Validated ApplyRequest request) {

		if (!securityService.hasRole("STUDENT")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", "NO"));
		}
		studentEmployerService.request(recruitmentId, employerId, request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Gửi CV thành công", "OK"));
	}

	@DeleteMapping("/cancel")
	public ResponseEntity<ApiResponse<String>> cancelApply(@RequestParam int studentId, @RequestParam int employerId) {
		if (!securityService.hasRole("STUDENT")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		studentEmployerService.cancelApply(studentId, employerId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Hủy thành công", "OK"));
	}

	// Chấp nhận sinh viên
	@PutMapping("/approve")
	public ResponseEntity<ApiResponse<String>> request(@RequestParam int studentId, @RequestBody String reply) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", "NO"));
		}
		studentEmployerService.approve(studentId, reply);
		return ResponseEntity.ok(new ApiResponse<>(true, "Chấp nhận thành công", "OK"));
	}

	// Từ chối sinh viên
	@PutMapping("/reject")
	public ResponseEntity<ApiResponse<String>> reject(@RequestParam int studentId, @RequestBody String reply) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", "NO"));
		}
		studentEmployerService.reject(studentId, reply);
		return ResponseEntity.ok(new ApiResponse<>(true, "Từ chối thành công", "OK"));
	}

	// Lấy ra danh sách sinh viên ứng tuyển
	@GetMapping("/get-reply")
	public ResponseEntity<ApiResponse<String>> getReply(@RequestParam int studentId, @RequestParam int employerId,
			@RequestParam String status,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeUpdate) {
		if (!securityService.hasRole("STUDENT")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		String response = studentEmployerService.getReply(studentId, employerId, status, timeUpdate);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Lấy ra danh sách sinh viên ứng tuyển
	@GetMapping("/get-all-apply")
	public ResponseEntity<ApiResponse<List<ApplyInfoResponse>>> getAllApply() {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ApplyInfoResponse> response = studentEmployerService.getAllApply();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xem chi tiết thông tin sinh viên ứng tuyển
	@GetMapping("/get-apply-info")
	public ResponseEntity<ApiResponse<ApplyInfoResponse>> getApplyStudentEmployerInfo(@RequestParam int studentId) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		ApplyInfoResponse response = studentEmployerService.getApplyStudentEmployerInfo(studentId);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}
	
	@GetMapping("/get-apply-info-v2")
	public ResponseEntity<ApiResponse<ApplyInfoResponse>> getApplyStudentEmployerInfo(@RequestParam int studentId, @RequestParam int employerId) {
		ApplyInfoResponse response = studentEmployerService.getApplyStudentEmployerInfoV2(studentId, employerId);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xem chi tiết thông tin sinh viên ứng tuyển
	@GetMapping("/get-approve-info")
	public ResponseEntity<ApiResponse<ApplyInfoResponse>> getApproveStudentEmployerInfo(@RequestParam int studentId) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		ApplyInfoResponse response = studentEmployerService.getApproveStudentEmployerInfo(studentId);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Sinh viên xem chi tiết thông tin ứng tuyển
	@GetMapping("/get-approve-info-v2")
	public ResponseEntity<ApiResponse<ApplyInfoResponse>> getGetApproveDetail(@RequestParam int studentId,
			@RequestParam int employerId, @RequestParam String status) {
		if (!securityService.hasRole("STUDENT")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		ApplyInfoResponse response = studentEmployerService.getGetApproveDetail(studentId, employerId, status);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xem danh sách sinh viên được nhận
	@GetMapping("/get-all-internships")
	public ResponseEntity<ApiResponse<List<ListApprovedResponse>>> getAllInternships() {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListApprovedResponse> response = studentEmployerService.getAllInternships();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xem danh sách các hoạt động ứng tuyển
	@GetMapping("/get-all-applyed")
	public ResponseEntity<ApiResponse<List<ListApplyedResponse>>> getAllApplyed() {
		if (!securityService.hasRole("STUDENT")) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListApplyedResponse> response = studentEmployerService.getAllApplyed();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}
}
