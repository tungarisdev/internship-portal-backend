package com.iportal.dto.student_employer;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListApprovedResponse {
	private int id;
	
	private String fullName;

    private String classRoom;

    private String major;
    
    private LocalDateTime timeUpdate;
}
