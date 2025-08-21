package com.iportal.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
	private String oldPassword;
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
	private String newPassword;
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
	private String confirmPassword;
}
