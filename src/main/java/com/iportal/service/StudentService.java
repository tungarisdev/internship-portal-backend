package com.iportal.service;

import java.util.List;

import com.iportal.dto.student.ListStudentResponse;
import com.iportal.dto.student.ListUsernameResponse;
import com.iportal.dto.student.UpdateStudentByAdminRequest;
import com.iportal.dto.student.UpdateStudentRequest;
import com.iportal.dto.student.UpdateStudentResponse;
import com.iportal.dto.user.CreateSUserRequest;

public interface StudentService {
	void createSUser(CreateSUserRequest request);

	UpdateStudentResponse updateStudent(UpdateStudentRequest request);

	List<ListStudentResponse> getAllStudent();

	UpdateStudentResponse getStudentProfile();

	UpdateStudentResponse updateStudentByAdmin(String username, UpdateStudentByAdminRequest request);

	List<ListUsernameResponse> getAllStudentUsername();

	UpdateStudentResponse getStudentProfileByAdmin(String username);
}
