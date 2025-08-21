package com.iportal.dto.student;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iportal.utils.LocalDateDeserializer;
import com.iportal.utils.StringTrimToNullDeserializer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentByAdminRequest {
	
	@JsonDeserialize(using = StringTrimToNullDeserializer.class)
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;
	
	@Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại phải có 10 hoặc 11 ký tự")
    private String phoneNumber;
	
    @Size(max = 100, message = "Họ và tên không hợp lệ")
    private String fullName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Past(message = "Ngày sinh phải là quá khứ")
    private LocalDate dob;

    @JsonDeserialize(using = StringTrimToNullDeserializer.class)
    @Size(max = 100, message = "Tên lớp quá dài")
    private String classRoom;

    @Size(max = 100, message = "Chuyên ngành quá dài")
    private String major;

    //@Min(value = 1, message = "Year of study must be at least 1")
    private Integer yearOfStudy;

    @JsonDeserialize(using = StringTrimToNullDeserializer.class)
    @Size(max = 500, message = "Địa chỉ quá dài")
    private String address;
    
    @JsonDeserialize(using = StringTrimToNullDeserializer.class)
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email quá dài")
    private String email;
}
