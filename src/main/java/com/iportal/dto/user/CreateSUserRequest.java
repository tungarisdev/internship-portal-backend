package com.iportal.dto.user;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iportal.utils.LocalDateDeserializer;
import com.iportal.utils.StringTrimToNullDeserializer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSUserRequest {
	
    @NotBlank(message = "Hãy nhập username")
	@Size(min = 9, max = 9, message = "Username phải có 9 ký tự")
    private String username;
    
    @NotBlank(message = "Hãy nhập mật khẩu")
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;
    
	@NotBlank(message = "Hãy nhập số điện thoại")
	@Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại phải có 10 hoặc 11 ký tự")
    private String phoneNumber;

	@NotBlank(message = "Hãy nhập họ và tên")
    @Size(max = 255, message = "Họ và tên quá dài")
    private String fullName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Past(message = "Ngày sinh phải ở quá khứ")
    private LocalDate dob;	

    @JsonDeserialize(using = StringTrimToNullDeserializer.class)
    @Size(max = 100, message = "Tên lớp học quá dài")
    private String classRoom;

    @NotBlank(message = "Hãy nhập chuyên ngành")
    @Size(max = 100, message = "Chuyên ngành quá dài")
    private String major;

    @Min(value = 1, message = "Year of study must be at least 1")
    private int yearOfStudy;

    @JsonDeserialize(using = StringTrimToNullDeserializer.class)
    @Size(max = 500, message = "Địa chỉ quá dài")
    private String address;
    
    @JsonDeserialize(using = StringTrimToNullDeserializer.class)
    @Email(message = "Địa chỉ email không hợp lệ")
    @Size(max = 255, message = "Email quá dài")
    private String email;
}
