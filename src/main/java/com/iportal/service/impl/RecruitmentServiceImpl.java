package com.iportal.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.iportal.dto.recruitment.*;
import com.iportal.entity.Employer;
import com.iportal.entity.Recruitment;
import com.iportal.entity.User;
import com.iportal.repository.EmployerRepository;
import com.iportal.repository.RecruitmentRepository;
import com.iportal.repository.UserRepository;
import com.iportal.service.RecruitmentService;
import com.iportal.service.SecurityService;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class RecruitmentServiceImpl implements RecruitmentService {
    
    @Autowired
    private ObjectMapper objectMapper;

	private final RecruitmentRepository recruitmentRepository;
	private final EmployerRepository employerRepository;
	private final SecurityService securityService;
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;

	@Override
	public void createRecruitment(CreateRecruitmentRequest request) {
		if (request.getExpired().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Hạn nộp ứng tuyển phải lớn hơn hôm nay");
		}

		Employer employer = getCurrentEmployer();
		Recruitment recruitment = modelMapper.map(request, Recruitment.class);
		recruitment.setPostedDate(LocalDate.now());
		recruitment.setStatus("OPEN");
		recruitment.setEmployer(employer);
		recruitment.setRecruitmentStatus("PENDING");
		recruitment.setPosted(false);
		recruitmentRepository.save(recruitment);
	}

	@Override
	public void acceptRecruitment(int id) {
		Recruitment recruitment = recruitmentRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bài tuyển dụng"));
		User user = userRepository.findByUsername(securityService.getCurrentUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		recruitment.setRecruitmentStatus("ACCEPTED");
		recruitment.setIdUserVerify(user);
		recruitment.setPosted(true);
		recruitmentRepository.save(recruitment);
	}
	
	@Override
	public void rejectRecruitment(int id) {
	    Recruitment recruitment = recruitmentRepository.findById(id)
	            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bài tuyển dụng"));
	    User user = userRepository.findByUsername(securityService.getCurrentUsername())
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    if (recruitment.isPosted()) {
	        // Nếu bài đã được đăng chính thức, rollback về bản đã được duyệt gần nhất
	        if (recruitment.getDraftContentJson() != null) {
	            try {
	                Map<String, Object> draft = objectMapper.readValue(recruitment.getDraftContentJson(), Map.class);
	                recruitment.setTitle((String) draft.get("title"));
	                recruitment.setDescription((String) draft.get("description"));
	                recruitment.setRequirements((String) draft.get("requirements"));
	                recruitment.setBenefits((String) draft.get("benefits"));
	                recruitment.setSalaryRange(Double.parseDouble(draft.get("salaryRange").toString()));
	                recruitment.setAddress((String) draft.get("address"));
	                recruitment.setExpired(LocalDate.parse(draft.get("expired").toString()));
	                recruitment.setStatus((String) draft.get("status"));
	                recruitment.setDraftContentJson(null);
	            } catch (Exception e) {
	                throw new RuntimeException("Không thể khôi phục bản nháp bài tuyển dụng", e);
	            }
	        }
	    }

	    recruitment.setRecruitmentStatus("REJECTED");
	    recruitment.setIdUserVerify(user);
	    recruitmentRepository.save(recruitment);
	}

	
	@Override
	public void cancelRecruitment(int id) {
	    Recruitment recruitment = recruitmentRepository.findById(id)
	            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bài tuyển dụng"));
	    Employer employer = getCurrentEmployer();

	    if (recruitment.getEmployer().getId() != employer.getId()) {
	        throw new AccessDeniedException("Bạn không sở hữu bài tuyển dụng này");
	    }

	    if (recruitment.isPosted()) {
	        // Nếu đã được đăng chính thức, chỉ rollback dữ liệu từ draftContentJson nếu có
	        if (recruitment.getDraftContentJson() != null) {
	            try {
	                Map<String, Object> draft = objectMapper.readValue(recruitment.getDraftContentJson(), Map.class);
	                recruitment.setTitle((String) draft.get("title"));
	                recruitment.setDescription((String) draft.get("description"));
	                recruitment.setRequirements((String) draft.get("requirements"));
	                recruitment.setBenefits((String) draft.get("benefits"));
	                recruitment.setSalaryRange(Double.parseDouble(draft.get("salaryRange").toString()));
	                recruitment.setAddress((String) draft.get("address"));
	                recruitment.setExpired(LocalDate.parse(draft.get("expired").toString()));
	                recruitment.setStatus((String) draft.get("status"));
	                recruitment.setDraftContentJson(null);
	                recruitmentRepository.save(recruitment);
	            } catch (Exception e) {
	                throw new RuntimeException("Không thể khôi phục bản nháp bài tuyển dụng", e);
	            }
	        }
	    } else {
	        // Nếu chưa được đăng (PENDING lần đầu), xóa
	        recruitmentRepository.delete(recruitment);
	    }
	}


	@Override
	public UpdateRecruitmentResponse updateRecruitment(int id, UpdateRecruitmentRequest request) {
	    Recruitment recruitment = getRecruitmentById(id);
	    Employer employer = getCurrentEmployer();

	    if (recruitment.getEmployer().getId() != employer.getId()) {
	        throw new AccessDeniedException("Bạn không sở hữu bài tuyển dụng này");
	    }

	    if (request.getExpired().isBefore(LocalDate.now())) {
	        throw new IllegalArgumentException("Hạn nộp ứng tuyển phải lớn hơn hôm nay");
	    }

	    if (!isValidStatus(request.getStatus())) {
	        throw new IllegalArgumentException("Trạng thái không hợp lệ");
	    }

	    // Kiểm tra xem có thay đổi nội dung gì không (trừ status)
	    boolean isModified =
	            !Objects.equals(request.getTitle(), recruitment.getTitle()) ||
	            !Objects.equals(request.getDescription(), recruitment.getDescription()) ||
	            !Objects.equals(request.getRequirements(), recruitment.getRequirements()) ||
	            !Objects.equals(request.getBenefits(), recruitment.getBenefits()) ||
	            !Objects.equals(request.getSalaryRange(), recruitment.getSalaryRange()) ||
	            !Objects.equals(request.getAddress(), recruitment.getAddress()) ||
	            !Objects.equals(request.getExpired(), recruitment.getExpired());

	    if (isModified) {
	        // Lưu bản nháp
	        try {
	            Map<String, Object> draft = new HashMap<>();
	            draft.put("title", recruitment.getTitle());
	            draft.put("description", recruitment.getDescription());
	            draft.put("requirements", recruitment.getRequirements());
	            draft.put("benefits", recruitment.getBenefits());
	            draft.put("salaryRange", recruitment.getSalaryRange());
	            draft.put("address", recruitment.getAddress());
	            draft.put("expired", recruitment.getExpired());
	            draft.put("status", recruitment.getStatus());

	            String draftJson = objectMapper.writeValueAsString(draft);
	            recruitment.setDraftContentJson(draftJson);
	        } catch (Exception e) {
	            throw new RuntimeException("Không thể lưu bản nháp bài tuyển dụng", e);
	        }

	        // Chuyển về trạng thái chờ duyệt
	        recruitment.setRecruitmentStatus("PENDING");
	        recruitment.setPostedDate(LocalDate.now());
	    }

	    // Cập nhật tất cả fields
	    modelMapper.map(request, recruitment);
	    return modelMapper.map(recruitmentRepository.save(recruitment), UpdateRecruitmentResponse.class);
	}

	@Override
	public List<ListRecruitmentResponse> getAllByEmployer() {
		Employer employer = getCurrentEmployer();

		return recruitmentRepository.findByEmployer_idAndRecruitmentStatus(employer.getId(), "ACCEPTED").stream()
				.map(r -> toListRecruitmentResponse(r, employer)).collect(Collectors.toList());
	}

	@Override
	public List<ListRecruitmentResponse> getAll() {
		return recruitmentRepository.findAllByStatusAndRecruitmentStatus("OPEN", "ACCEPTED").stream()
				.map(r -> toListRecruitmentResponse(r, r.getEmployer())).collect(Collectors.toList());
	}

	@Override
	public List<ListRecruitmentResponse> getAllByAdmin() {
		return recruitmentRepository.findAllByRecruitmentStatus("ACCEPTED").stream()
				.map(r -> toListRecruitmentResponse(r, r.getEmployer())).collect(Collectors.toList());
	}
	
	@Override
	public List<ListRecruitmentResponse> getAllPendingRecruitment() {
		return recruitmentRepository.findAllByRecruitmentStatus("PENDING").stream()
				.map(r -> toListRecruitmentResponse(r, r.getEmployer())).collect(Collectors.toList());
	}
	
	@Override
	public List<ListRecruitmentResponse> getAllPendingRecruitmentByEmployer() {
		Employer employer = getCurrentEmployer();
		
		return recruitmentRepository.findAllByRecruitmentStatusAndEmployer_id("PENDING", employer.getId()).stream()
				.map(r -> toListRecruitmentResponse(r, r.getEmployer())).collect(Collectors.toList());
	}

	@Override
	public GetRecruitmentResponse getById(int recruitmentId) {
		Recruitment recruitment;
		try {
			recruitment = recruitmentRepository.findByIdAndRecruitmentStatus(recruitmentId, "ACCEPTED");
			if (recruitment == null && !securityService.hasRole("STUDENT")) {
				recruitment = recruitmentRepository.findByIdAndRecruitmentStatus(recruitmentId, "PENDING");
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException("Không tìm thấy bài tuyển dụng");
		}
		return toGetRecruitmentResponse(recruitment);
	}

	@Override
	public void deleteRecruitment(int id) {
		Recruitment recruitment = getRecruitmentById(id);
		Employer employer = getCurrentEmployer();

		if (recruitment.getEmployer().getId() != employer.getId()) {
			throw new AccessDeniedException("Quyền truy cập bị từ chối");
		}

		recruitmentRepository.deleteById(id);
	}

	@Override
	public void deleteRecruitmentByAdmin(int id) {
		Recruitment recruitment = getRecruitmentById(id);
		recruitmentRepository.delete(recruitment);
	}

	@Override
	public GetRecruitmentResponse getByIdByEmployer(int recruitmentId) {
		Employer employer = getCurrentEmployer();
		Recruitment recruitment = recruitmentRepository.findByRecruitment(recruitmentId)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bài tuyển dụng"));

		if (recruitment.getEmployer().getId() != employer.getId()) {
			throw new AccessDeniedException("Quyền truy cập bị từ chối");
		}

		return toGetRecruitmentResponse(recruitment);
	}

	// === Private Helpers ===

	private Employer getCurrentEmployer() {
		return employerRepository.findByUser_username(securityService.getCurrentUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhà tuyển dụng"));
	}

	private Recruitment getRecruitmentById(int id) {
		return recruitmentRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bài tuyển dụng"));
	}

	private boolean isValidStatus(String status) {
		return "OPEN".equals(status) || "CLOSED".equals(status);
	}

	private ListRecruitmentResponse toListRecruitmentResponse(Recruitment recruitment, Employer employer) {
		ListRecruitmentResponse response = modelMapper.map(recruitment, ListRecruitmentResponse.class);
		response.setCompanyName(employer.getCompanyName());
		response.setEmployerId(employer.getId());
		return response;
	}

	private GetRecruitmentResponse toGetRecruitmentResponse(Recruitment recruitment) {
		GetRecruitmentResponse response = modelMapper.map(recruitment, GetRecruitmentResponse.class);
		response.setCompanyName(recruitment.getEmployer().getCompanyName());
		response.setEmployerId(recruitment.getEmployer().getId());
		return response;
	}
}
