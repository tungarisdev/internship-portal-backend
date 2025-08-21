package com.iportal.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iportal.dto.student_employer.ApplyInfoResponse;
import com.iportal.dto.student_employer.ApplyRequest;
import com.iportal.dto.student_employer.ListApplyedResponse;
import com.iportal.dto.student_employer.ListApprovedResponse;
import com.iportal.entity.Employer;
import com.iportal.entity.Recruitment;
import com.iportal.entity.Student;
import com.iportal.entity.StudentEmployer;
import com.iportal.repository.EmployerRepository;
import com.iportal.repository.RecruitmentRepository;
import com.iportal.repository.StudentEmployerRepository;
import com.iportal.repository.StudentRepository;
import com.iportal.service.SecurityService;
import com.iportal.service.StudentEmployerService;

@Service
public class StudentEmployerServiceImpl implements StudentEmployerService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private StudentEmployerRepository studentEmployerRepository;

    @Override
    public void request(int recruitmentId, int employerId, ApplyRequest request) {
        Recruitment recruitment = findRecruitmentById(recruitmentId);
        validateRecruitment(recruitment);

        Student student = getCurrentStudent();
        Employer employer = findEmployerById(employerId);

        if (studentEmployerRepository.existsByStudentIdAndEmployerIdWithStatus(student.getId(), employerId)) {
            throw new InternalAuthenticationServiceException(
                    "Bạn đã có đơn đăng ký được chấp thuận và không thể đăng ký thêm.");
        }

        StudentEmployer studentEmployer = modelMapper.map(request, StudentEmployer.class);
        studentEmployer.setStudent(student);
        studentEmployer.setEmployer(employer);
        studentEmployer.setStatus("PENDING");
        studentEmployer.setTimeUpdate(LocalDateTime.now());
        studentEmployerRepository.save(studentEmployer);
    }

    @Override
    public void cancelApply(int studentId, int employerId) {
        if (studentEmployerRepository.existsByStudentIdWithApproveStatus(studentId)) {
            throw new InternalAuthenticationServiceException("Không thể hủy đơn do trạng thái đã được chấp thuận!");
        }

        StudentEmployer studentEmployer = studentEmployerRepository
                .findApplyedByStudentIdAndEmployerIdAndStatus(studentId, employerId, "PENDING")
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy đơn ứng tuyển."));

        studentEmployerRepository.delete(studentEmployer);
    }

    @Override
    public void approve(int studentId, String reply) {
        Employer employer = getCurrentEmployer();
        StudentEmployer studentEmployer = findStudentEmployer(studentId, employer.getId());
        updateStudentEmployerStatus(studentEmployer, "APPROVE", reply);
    }

    @Override
    public void reject(int studentId, String reply) {
        Employer employer = getCurrentEmployer();
        StudentEmployer studentEmployer = findStudentEmployer(studentId, employer.getId());
        updateStudentEmployerStatus(studentEmployer, "REJECT", reply);
    }

    @Override
    public String getReply(int studentId, int employerId, String status, LocalDateTime timeUpdate) {
        return studentEmployerRepository.getReply(studentId, employerId, status, timeUpdate);
    }

    @Override
    public List<ApplyInfoResponse> getAllApply() {
        Employer employer = getCurrentEmployer();
        return studentEmployerRepository.findByEmployerIdAndStatus(employer.getId(), "PENDING")
                .stream()
                .map(this::mapToApplyInfoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApplyInfoResponse getApplyStudentEmployerInfo(int studentId) {
        Employer employer = getCurrentEmployer();
        return mapToApplyInfoResponse(findStudentEmployer(studentId, employer.getId()));
    }

    @Override
    public ApplyInfoResponse getApplyStudentEmployerInfoV2(int studentId, int employerId) {
        return mapToApplyInfoResponse(findStudentEmployer(studentId, employerId));
    }

    @Override
    public ApplyInfoResponse getGetApproveDetail(int studentId, int employerId, String status) {
        return mapToApplyInfoResponse(findStudentEmployerByStatus(studentId, employerId, status));
    }

    @Override
    public List<ListApprovedResponse> getAllInternships() {
        Employer employer = getCurrentEmployer();
        return studentEmployerRepository.findByEmployerIdAndStatus(employer.getId(), "APPROVE")
                .stream()
                .map(this::mapToListApprovedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApplyInfoResponse getApproveStudentEmployerInfo(int studentId) {
        Employer employer = getCurrentEmployer();
        return mapToApplyInfoResponse(studentEmployerRepository
                .findByStudentIdAndEmployerIdWithStatus2(studentId, employer.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy dữ liệu")));
    }

    @Override
    public List<ListApplyedResponse> getAllApplyed() {
        Student student = getCurrentStudent();
        return studentEmployerRepository.findByStudentId(student.getId())
                .stream()
                .map(this::mapToListApplyedResponse)
                .collect(Collectors.toList());
    }

    // === Private Helpers ===
    
    private Student getCurrentStudent() {
        return studentRepository.findByUser_username(securityService.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy sinh viên"));
    }

    private Employer getCurrentEmployer() {
        return employerRepository.findByUser_username(securityService.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhà tuyển dụng"));
    }

    private Recruitment findRecruitmentById(int recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bài tuyển dụng"));
    }

    private Employer findEmployerById(int employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhà tuyển dụng"));
    }

    private StudentEmployer findStudentEmployer(int studentId, int employerId) {
        return studentEmployerRepository.findByStudentIdAndEmployerIdWithStatus(studentId, employerId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy dữ liệu"));
    }

    private StudentEmployer findStudentEmployerByStatus(int studentId, int employerId, String status) {
        return studentEmployerRepository
                .findApplyedByStudentIdAndEmployerIdAndStatus(studentId, employerId, status)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy dữ liệu"));
    }

    private void validateRecruitment(Recruitment recruitment) {
        if ("CLOSED".equals(recruitment.getStatus())) {
            throw new InternalAuthenticationServiceException("Bài tuyển dụng đã đóng.");
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate expiredDate = recruitment.getExpired();
        if (expiredDate != null && currentDate.isAfter(expiredDate)) {
            throw new InternalAuthenticationServiceException("Bài tuyển dụng đã hết hạn vào " + expiredDate);
        }
    }

    private void updateStudentEmployerStatus(StudentEmployer studentEmployer, String status, String reply) {
        studentEmployer.setStatus(status);
        studentEmployer.setReply(reply);
        studentEmployer.setTimeUpdate(LocalDateTime.now());
        studentEmployerRepository.save(studentEmployer);
    }

    private ApplyInfoResponse mapToApplyInfoResponse(StudentEmployer studentEmployer) {
        ApplyInfoResponse response = modelMapper.map(studentEmployer, ApplyInfoResponse.class);
        Student student = studentEmployer.getStudent();
        response.setStudentId(student.getId());
        response.setFullName(student.getFullName());
        response.setYearOfStudy(student.getYearOfStudy());
        response.setMajor(student.getMajor());
        return response;
    }

    private ListApprovedResponse mapToListApprovedResponse(StudentEmployer studentEmployer) {
        ListApprovedResponse response = modelMapper.map(studentEmployer, ListApprovedResponse.class);
        Student student = studentEmployer.getStudent();
        response.setId(student.getId());
        response.setFullName(student.getFullName());
        response.setClassRoom(student.getClassRoom());
        response.setMajor(student.getMajor());
        return response;
    }

    private ListApplyedResponse mapToListApplyedResponse(StudentEmployer studentEmployer) {
        ListApplyedResponse response = modelMapper.map(studentEmployer, ListApplyedResponse.class);
        response.setStudentId(studentEmployer.getStudent().getId());
        response.setCompanyName(studentEmployer.getEmployer().getCompanyName());
        response.setEmployerId(studentEmployer.getEmployer().getId());
        return response;
    }
}