package com.iportal.dto.recruitment;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iportal.utils.LocalDateDeserializer;
import com.iportal.utils.StringTrimToNullDeserializer;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecruitmentRequest {
	
	@JsonDeserialize(using = StringTrimToNullDeserializer.class)
	@Size(min = 5, message = "Tiêu đề quá ngắn")
	private String title;
	
	@JsonDeserialize(using = StringTrimToNullDeserializer.class)
	@Size(min = 50, message = "Mô tả công việc quá ngắn")
	private String description;
	
	@JsonDeserialize(using = StringTrimToNullDeserializer.class)
	@Size(min = 50, message = "Yêu cầu công việc quá ngắn")
	private String requirements;
	
	@JsonDeserialize(using = StringTrimToNullDeserializer.class)
	@Size(min = 20, message = "Quyền lợi quá ngắn")
	private String benefits;
	
	@DecimalMin(value = "500000.0", message = "Mức lương quá thấp")
    @DecimalMax(value = "100000000.0", message = "Mức lương quá cao")
	private Double salaryRange;
	
	
	@Size(min = 10, message = "Địa chỉ chưa cụ thể")
	private String address;
	
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate expired;
	
	@JsonDeserialize(using = StringTrimToNullDeserializer.class)
	private String status;
}
