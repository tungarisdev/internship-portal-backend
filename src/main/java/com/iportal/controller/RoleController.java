package com.iportal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {
	// Bỏ bởi vì khi tạo kho sẽ tạo luôn vai trò
//	
////	@GetMapping("/get-all")
////	public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRole() {
////		List<RoleResponse> responses = roleService.getAllRole();
////		return ResponseEntity.ok(new ApiResponse<>(true, "Get successfully", responses));
////	}
////	
////	@PostMapping("/create")
////	public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest request) {
////		if (!securityService.hasRole("MANAGER")) {
////			return ResponseEntity.ok(new ApiResponse<>(false, "Access denied", null));
////		}
////		RoleResponse response = roleService.create(request);
////		return ResponseEntity.ok(new ApiResponse<>(true, "Create role successfully", response));
////	}
////	
}
