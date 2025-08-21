package com.iportal.dto.employer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployerByAdminRequest {
	
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;
	
	@Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại phải có 10 hoặc 11 ký tự")
    private String phoneNumber;
	
    @Size(max = 255, message = "Tên công ty quá dài")
    private String companyName;

    @Size(max = 500, message = "Địa chỉ quá dài")
    private String address;
    
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email quá dài")
    private String email;
    
    @Size(min = 10, message = "Địa chỉ website quá ngắn")
    private String website;
}
