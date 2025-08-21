package com.iportal.dto.student_employer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyInfoResponse {
	private int studentId;
	
	private String fullName;
	
	private int yearOfStudy;
	
	private String major;
	
	private String title;
	
	private String cvLink;
}
