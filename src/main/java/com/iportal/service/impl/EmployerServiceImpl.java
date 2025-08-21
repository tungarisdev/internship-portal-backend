package com.iportal.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iportal.dto.employer.ListEmployerResponse;
import com.iportal.dto.employer.ListUsernameResponse;
import com.iportal.dto.employer.UpdateEmployerByAdminRequest;
import com.iportal.dto.employer.UpdateEmployerRequest;
import com.iportal.dto.employer.UpdateEmployerResponse;
import com.iportal.dto.user.CreateEUserRequest;
import com.iportal.entity.Employer;
import com.iportal.entity.Role;
import com.iportal.entity.User;
import com.iportal.repository.EmployerRepository;
import com.iportal.repository.RoleRepository;
import com.iportal.repository.UserRepository;
import com.iportal.service.EmployerService;
import com.iportal.service.SecurityService;

@Service
public class EmployerServiceImpl implements EmployerService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerRepository employerRepository;

    private static final String ROLE_EMPLOYER = "EMPLOYER";

    @Override
    @Transactional
    public void createEUser(CreateEUserRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus("INACTIVE");

        Role role = roleRepository.findByRoleName(ROLE_EMPLOYER)
                .orElseThrow(() -> new IllegalArgumentException("Role undefined"));
        user.setRole(role);

        user = userRepository.save(user);

        Employer employer = modelMapper.map(request, Employer.class);
        employer.setUser(user);
        employerRepository.save(employer);
    }

    @Override
    @Transactional
    public UpdateEmployerResponse updateEmployer(UpdateEmployerRequest request) {
        Employer employer = getCurrentEmployer();
        User user = employer.getUser();

        modelMapper.map(request, user);
        userRepository.save(user);

        modelMapper.map(request, employer);
        employerRepository.save(employer);

        return modelMapper.map(employer, UpdateEmployerResponse.class);
    }

    @Override
    @Transactional
    public UpdateEmployerResponse updateEmployerByAdmin(String username, UpdateEmployerByAdminRequest request) {
        Employer employer = getCurrentEmployer();
        User user = employer.getUser();

        modelMapper.map(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        modelMapper.map(request, employer);
        employerRepository.save(employer);

        return modelMapper.map(employer, UpdateEmployerResponse.class);
    }

    @Override
    public List<ListEmployerResponse> getAllEmployer() {
        return employerRepository.getAllEmployers();
    }

    @Override
    public List<ListUsernameResponse> getAllUsername() {
        return employerRepository.getAllUsername();
    }

    @Override
    public UpdateEmployerResponse getEmployerProfile() {
        Employer employer = getCurrentEmployer();
        return modelMapper.map(employer, UpdateEmployerResponse.class);
    }

    @Override
    public UpdateEmployerResponse getEmployerProfileByAdmin(String username) {
        Employer employer = employerRepository.findByUser_username(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhà tuyển dụng"));
        return modelMapper.map(employer, UpdateEmployerResponse.class);
    }

    // === Private helper ===

    private Employer getCurrentEmployer() {
    	System.out.println(securityService.getCurrentUsername());
        return employerRepository.findByUser_username(securityService.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhà tuyển dụng"));
    }
}
