package com.iportal.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iportal.dto.student.*;
import com.iportal.dto.user.CreateSUserRequest;
import com.iportal.entity.Role;
import com.iportal.entity.Student;
import com.iportal.entity.User;
import com.iportal.repository.RoleRepository;
import com.iportal.repository.StudentRepository;
import com.iportal.repository.UserRepository;
import com.iportal.service.SecurityService;
import com.iportal.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String ROLE_STUDENT = "STUDENT";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    @Transactional
    public void createSUser(CreateSUserRequest request) {
        if (request.getDob() != null && request.getDob().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ: không thể ở tương lai");
        }

        User user = modelMapper.map(request, User.class);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus("INACTIVE");

        Role role = roleRepository.findByRoleName(ROLE_STUDENT)
                .orElseThrow(() -> new IllegalArgumentException("Role undefined"));
        user.setRole(role);
        user = userRepository.save(user);

        Student student = modelMapper.map(request, Student.class);
        student.setUser(user);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public UpdateStudentResponse updateStudent(UpdateStudentRequest request) {
        Student student = getCurrentStudent();
        User user = student.getUser();

        modelMapper.map(request, user);
        userRepository.save(user);

        modelMapper.map(request, student);
        studentRepository.save(student);

        return modelMapper.map(student, UpdateStudentResponse.class);
    }

    @Override
    @Transactional
    public UpdateStudentResponse updateStudentByAdmin(String username, UpdateStudentByAdminRequest request) {
        Student student = getStudentByUsername(username);
        User user = student.getUser();

        modelMapper.map(request, user);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        modelMapper.map(request, student);
        studentRepository.save(student);

        return modelMapper.map(student, UpdateStudentResponse.class);
    }

    @Override
    public List<ListStudentResponse> getAllStudent() {
        return studentRepository.getAllStudents();
    }

    @Override
    public List<ListUsernameResponse> getAllStudentUsername() {
        return studentRepository.getAllUsername();
    }

    @Override
    public UpdateStudentResponse getStudentProfile() {
        Student student = getCurrentStudent();
        return modelMapper.map(student, UpdateStudentResponse.class);
    }

    @Override
    public UpdateStudentResponse getStudentProfileByAdmin(String username) {
        Student student = getStudentByUsername(username);
        return modelMapper.map(student, UpdateStudentResponse.class);
    }

    // === Private helpers ===

    private Student getCurrentStudent() {
        return studentRepository.findByUser_username(securityService.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy sinh viên"));
    }

    private Student getStudentByUsername(String username) {
        return studentRepository.findByUser_username(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy sinh viên"));
    }
}
