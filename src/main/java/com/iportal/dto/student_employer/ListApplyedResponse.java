package com.iportal.dto.student_employer;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListApplyedResponse {
	private int studentId;
	private String companyName;
	private String status;
	private int employerId;
	private LocalDateTime timeUpdate;
}
