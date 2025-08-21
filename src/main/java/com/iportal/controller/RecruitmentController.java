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
import com.iportal.dto.recruitment.CreateRecruitmentRequest;
import com.iportal.dto.recruitment.GetRecruitmentResponse;
import com.iportal.dto.recruitment.ListRecruitmentResponse;
import com.iportal.dto.recruitment.UpdateRecruitmentRequest;
import com.iportal.dto.recruitment.UpdateRecruitmentResponse;
import com.iportal.service.RecruitmentService;
import com.iportal.service.SecurityService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/recruitment")
public class RecruitmentController {
	@Autowired
	private SecurityService securityService;

	@Autowired
	private RecruitmentService recruitmentService;

	// Tạo bài tuyển dụng
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<String>> createRecruitment(
			@RequestBody @Validated CreateRecruitmentRequest request) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		recruitmentService.createRecruitment(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Tạo bài tuyển dụng thành công", "OK"));
	}

	// Duyệt bài tuyển dụng
	@PatchMapping("/accept/{id}")
	public ResponseEntity<ApiResponse<String>> acceptRecruitment(@PathVariable int id) {
		if (!securityService.hasRole("ADMIN")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		recruitmentService.acceptRecruitment(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Duyệt bài tuyển dụng thành công", "OK"));
	}

	// Từ chối bài tuyển dụng
	@PatchMapping("/reject/{id}")
	public ResponseEntity<ApiResponse<String>> rejectRecruitment(@PathVariable int id) {
		if (!securityService.hasRole("ADMIN")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		recruitmentService.rejectRecruitment(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Từ chối bài tuyển dụng thành công", "OK"));
	}

	// Từ chối bài tuyển dụng
	@PatchMapping("/cancel/{id}")
	public ResponseEntity<ApiResponse<String>> cancelRecruitment(@PathVariable int id) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		recruitmentService.cancelRecruitment(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Hủy duyệt bài tuyển dụng thành công", "OK"));
	}

	// Cập nhật thông tin tuyển dụng
	@PatchMapping("/update/{id}")
	public ResponseEntity<ApiResponse<UpdateRecruitmentResponse>> updateRecruitment(@PathVariable int id,
			@RequestBody @Validated UpdateRecruitmentRequest request) {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		UpdateRecruitmentResponse response = recruitmentService.updateRecruitment(id, request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật bài tuyển dụng thành công", response));
	}

	// Lấy ra toàn bộ bài tuyển dụng của nhà tuyển dụng đó
	@GetMapping("/get-all-by-employerid")
	public ResponseEntity<ApiResponse<List<ListRecruitmentResponse>>> getAllByEmployer() {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		List<ListRecruitmentResponse> response = recruitmentService.getAllByEmployer();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xóa bài tuyển dụng
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<String>> deleteRecruitment(@RequestParam int id) {
		if (securityService.hasRole("STUDENT")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		if (securityService.hasRole("ADMIN") || securityService.hasRole("MANAGER")) {
			// Nếu là quản trị viên thì xóa bài nào cũng được
			recruitmentService.deleteRecruitmentByAdmin(id);
		} else {
			// Nếu là EMPLOYER tự xóa thì sẽ chỉ có thể xóa bài mình
			recruitmentService.deleteRecruitment(id);
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "Xóa bài tuyển dụng thành công", "OK"));
	}

	// Lấy ra toàn bộ bài tuyển dụng
	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<List<ListRecruitmentResponse>>> getAll() {
		List<ListRecruitmentResponse> response;
		if (securityService.hasRole("ADMIN")) {
			// Có nghĩa là kể cả các bài để riêng tư admin cũng xem được
			response = recruitmentService.getAllByAdmin();
		} else {
			// Chỉ có thể lấy ra các bài đang để public
			response = recruitmentService.getAll();
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Lấy ra toàn bộ yêu cầu đăng bài của 
	@GetMapping("/get-all-request-recruitment-by-employer")
	public ResponseEntity<ApiResponse<List<ListRecruitmentResponse>>> getAllPendingRecruitmentByEmployer() {
		if (!securityService.hasRole("EMPLOYER")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		// Lấy ra các bài tuyển dụng của công ty với trạng thái là đang chờ duyệt (PENDING)
		List<ListRecruitmentResponse> response = recruitmentService.getAllPendingRecruitmentByEmployer();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Lấy ra toàn bộ yêu cầu đăng bài
	@GetMapping("/get-all-request-recruitment")
	public ResponseEntity<ApiResponse<List<ListRecruitmentResponse>>> getAllPendingRecruitment() {
		if (!securityService.hasRole("ADMIN")) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Access denied", null));
		}
		// Lấy ra toàn bộ các bài tuyển dụng của các công ty với trạng thái là đang chờ duyệt (PENDING)
		List<ListRecruitmentResponse> response = recruitmentService.getAllPendingRecruitment();
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}

	// Xem chi tiết nội dung bài tuyển dụng
	@GetMapping("/get-info")
	public ResponseEntity<ApiResponse<GetRecruitmentResponse>> getById(@RequestParam int recruitmentId) {
		GetRecruitmentResponse response = recruitmentService.getById(recruitmentId);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}
	
	// EMPLOYER tự xem thông tin bài tuyển dụng của mình
	@GetMapping("/e-get-info")
	public ResponseEntity<ApiResponse<GetRecruitmentResponse>> getByIdByEmployer(@RequestParam int recruitmentId) {
		GetRecruitmentResponse response = recruitmentService.getByIdByEmployer(recruitmentId);
		return ResponseEntity.ok(new ApiResponse<>(true, "OK", response));
	}
}
