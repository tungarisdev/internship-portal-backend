package com.iportal.dto.recruitment;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListRecruitmentResponse {
	
	private int id;
	
	private String title;
	
	private String companyName;
	
	private String address;
	
	private Double salaryRange;
	
	private LocalDate expired;
	
	private int employerId;
	
	// Mục đích là hiển thị status cho riêng employer đó khi get recruitment by employer_id (Lười tạo file mới)
	private String status;
}
