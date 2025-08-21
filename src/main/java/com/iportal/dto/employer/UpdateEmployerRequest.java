package com.iportal.dto.employer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployerRequest {
	
	@NotBlank(message = "Hãy nhập số điện thoại")
	@Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại phải có 10 hoặc 11 ký tự")
    private String phoneNumber;

    @NotBlank(message = "Hãy nhập địa chỉ")
	@Size(min = 10, max = 200, message = "Hãy nhập địa chỉ phù hợp")
    private String address;
    
    @NotBlank(message = "Hãy nhập email")
    @Email(message = "Định dạng email không hợp lệ")
    @Size(max = 255, message = "Email quá dài")
    private String email;
    
    @Size(min = 10, message = "Địa chỉ website quá dài")
    private String website;
}
