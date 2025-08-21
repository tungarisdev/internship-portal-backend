package com.iportal.service;

import java.util.List;

import com.iportal.dto.recruitment.CreateRecruitmentRequest;
import com.iportal.dto.recruitment.GetRecruitmentResponse;
import com.iportal.dto.recruitment.ListRecruitmentResponse;
import com.iportal.dto.recruitment.UpdateRecruitmentRequest;
import com.iportal.dto.recruitment.UpdateRecruitmentResponse;

public interface RecruitmentService {
	void createRecruitment(CreateRecruitmentRequest request);

	UpdateRecruitmentResponse updateRecruitment(int id, UpdateRecruitmentRequest request);

	List<ListRecruitmentResponse> getAllByEmployer();

	void deleteRecruitment(int id);

	List<ListRecruitmentResponse> getAll();

	GetRecruitmentResponse getById(int recruitmentId);

	GetRecruitmentResponse getByIdByEmployer(int recruitmentId);

	void deleteRecruitmentByAdmin(int id);

	List<ListRecruitmentResponse> getAllByAdmin();

	void acceptRecruitment(int id);

	void rejectRecruitment(int id);

	void cancelRecruitment(int id);

	List<ListRecruitmentResponse> getAllPendingRecruitment();

	List<ListRecruitmentResponse> getAllPendingRecruitmentByEmployer();
}
