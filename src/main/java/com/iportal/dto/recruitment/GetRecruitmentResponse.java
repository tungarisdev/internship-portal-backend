package com.iportal.dto.recruitment;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRecruitmentResponse {
	private int id;
	
	private String title;
	
	private String companyName;
	
	private String description;
	 
	private String requirements;
	
	private String benefits;
	
	private Double salaryRange;
	
	private String address;
	
	private LocalDate expired;
	
	private String status;
	
	private int employerId;
	
	private String recruitmentStatus;
}
