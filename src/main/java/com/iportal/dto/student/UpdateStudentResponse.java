package com.iportal.dto.student;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentResponse {
	private String fullName;

    private LocalDate dob;

    private String classRoom;

    private String major;

    private int yearOfStudy;

    private String address;
    
    private String email;
    
    private String phoneNumber;
}
