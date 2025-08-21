package com.iportal.service;

import java.util.List;

import com.iportal.dto.employer.ListEmployerResponse;
import com.iportal.dto.employer.ListUsernameResponse;
import com.iportal.dto.employer.UpdateEmployerByAdminRequest;
import com.iportal.dto.employer.UpdateEmployerRequest;
import com.iportal.dto.employer.UpdateEmployerResponse;
import com.iportal.dto.user.CreateEUserRequest;

public interface EmployerService {
	void createEUser(CreateEUserRequest request);

	UpdateEmployerResponse updateEmployer(UpdateEmployerRequest request);

	List<ListEmployerResponse> getAllEmployer();

	UpdateEmployerResponse getEmployerProfile();

	UpdateEmployerResponse updateEmployerByAdmin(String username, UpdateEmployerByAdminRequest request);

	List<ListUsernameResponse> getAllUsername();

	UpdateEmployerResponse getEmployerProfileByAdmin(String username);
}
