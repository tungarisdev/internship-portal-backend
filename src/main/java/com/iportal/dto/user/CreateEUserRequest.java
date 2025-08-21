package com.iportal.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEUserRequest {

    @NotBlank(message = "Hãy nhập username")
	@Size(min = 9, max = 9, message = "Username phải có 9 ký tự")
    private String username;
    
    @NotBlank(message = "Hãy nhập mật khẩu")
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;
    
    @NotBlank(message = "Hãy nhập số điện thoại")
    @Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại phải có 10 hoặc 11 ký tự")
    private String phoneNumber;
    
    @NotBlank(message = "Hãy nhập tên công ty")
	@Size(min = 5, max = 70, message = "Tên công ty không hợp lệ")
    private String companyName;
    
    @NotBlank(message = "Hãy nhập địa chỉ")
	@Size(min = 10, max = 200, message = "Địa chỉ không hợp lệ")
    private String address;
    
    @NotBlank(message = "Hãy nhập email")
    @Email(message = "Địa chỉ email không hợp lệ")
    @Size(max = 255, message = "Email quá dài")
    private String email;
    
    @NotBlank(message = "Hãy nhập địa chỉ website")
    @Size(min = 10, message = "Địa chỉ website không hợp lệ")
    private String website;
}
