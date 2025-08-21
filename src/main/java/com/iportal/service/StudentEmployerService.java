package com.iportal.service;

import java.time.LocalDateTime;
import java.util.List;

import com.iportal.dto.student_employer.ApplyInfoResponse;
import com.iportal.dto.student_employer.ApplyRequest;
import com.iportal.dto.student_employer.ListApplyedResponse;
import com.iportal.dto.student_employer.ListApprovedResponse;

public interface StudentEmployerService {

	void request(int recruitmentId, int employerId, ApplyRequest request);
	
	void approve(int studentId, String reply);

	void reject(int studentId, String reply);

	List<ApplyInfoResponse> getAllApply();

	ApplyInfoResponse getApplyStudentEmployerInfo(int studentId);

	ApplyInfoResponse getApplyStudentEmployerInfoV2(int studentId, int employerId);

	List<ListApprovedResponse> getAllInternships();

	ApplyInfoResponse getApproveStudentEmployerInfo(int studentId);

	List<ListApplyedResponse> getAllApplyed();

	ApplyInfoResponse getGetApproveDetail(int studentId, int employerId, String status);

	String getReply(int studentId, int employerId, String status, LocalDateTime timeUpdate);

	void cancelApply(int studentId, int employerId);
}
