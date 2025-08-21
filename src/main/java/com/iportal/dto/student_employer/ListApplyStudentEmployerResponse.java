package com.iportal.dto.student_employer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListApplyStudentEmployerResponse {
	private int studentId;
	private String title;
	private String cvLink;
}
