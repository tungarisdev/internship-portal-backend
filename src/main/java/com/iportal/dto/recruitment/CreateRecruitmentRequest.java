package com.iportal.dto.recruitment;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iportal.utils.LocalDateDeserializer;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecruitmentRequest {
	@NotBlank(message = "Hãy nhập tiêu đề")
	@Size(min = 5, message = "Tiêu đề quá ngắn")
	private String title;
	
	@NotBlank(message = "Hãy nhập mô tả công việc")
	@Size(min = 50, message = "Mô tả công việc quá ngắn")
	private String description;
	
	@NotBlank(message = "Hãy nhập yêu cầu công việc")
	@Size(min = 50, message = "Yêu cầu công việc quá ngắn")
	private String requirements;
	
	@NotBlank(message = "Hãy nhập quyền lợi")
	@Size(min = 20, message = "Quyền lợi quá ít")
	private String benefits;
	
	@NotNull(message = "Hãy nhập mức lương")
	@DecimalMin(value = "500000.0", message = "Mức lương quá thấp")
    @DecimalMax(value = "100000000.0", message = "Mức lương quá cao")
	private Double salaryRange;
	
	@NotBlank(message = "Hãy nhập địa chỉ")
	@Size(min = 10, message = "Địa chỉ chưa cụ thể")
	private String address;
	
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@NotNull(message = "Hãy nhập hạn nộp ứng tuyển")
	private LocalDate expired;
}
