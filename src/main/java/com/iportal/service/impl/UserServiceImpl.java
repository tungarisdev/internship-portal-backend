package com.iportal.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iportal.dto.user.ChangePasswordRequest;
import com.iportal.entity.User;
import com.iportal.repository.UserRepository;
import com.iportal.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public boolean changePassword(String username, ChangePasswordRequest request) {
        User user = getUserByUsername(username);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void banAccount(String username) {
        User user = getUserByUsername(username);
        user.setStatus("RESIGNED");
        userRepository.save(user);
    }

    @Override
    public void unbanAccount(String username) {
        User user = getUserByUsername(username);
        user.setStatus("INACTIVE");
        userRepository.save(user);
    }

    // === Private helper ===

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));
    }
}
