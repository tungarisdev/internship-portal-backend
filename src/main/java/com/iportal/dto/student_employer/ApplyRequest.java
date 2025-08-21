package com.iportal.dto.student_employer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyRequest {
	@NotBlank(message = "Hãy nhập lời ngỏ")
	@Size(min = 30, max = 5000, message = "Lời ngỏ phải dài hơn 30 ký tự")
	private String title;
	
	@NotBlank(message = "Hãy nhập link cv")
	@Size(min = 5, max = 500, message = "Link cv không hợp lệ")
	private String cvLink;
}
