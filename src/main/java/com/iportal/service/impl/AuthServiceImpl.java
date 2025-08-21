package com.iportal.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iportal.dto.auth.LoginRequest;
import com.iportal.dto.auth.LoginResponse;
import com.iportal.entity.Token;
import com.iportal.repository.TokenRepository;
import com.iportal.security.JwtUtil;
import com.iportal.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final long REFRESH_TOKEN_VALIDITY_MS = 259200000;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
    	String username = request.getUsername();
    	
        // Xác thực người dùng
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Tạo Access JWT token
        String accessToken = jwtUtil.generateAccessToken(username);
        // Tạo Refresh JWT token
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // Tạo thời gian hết hạn
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY_MS);

        // Lưu token mới
        tokenRepository.save(new Token(username, refreshToken, expiryDate, false, false));

        // Lấy vai trò người dùng (giả định chỉ có 1 role)
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User has no roles assigned"))
                .getAuthority();

        // Trả về thông tin đăng nhập
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .role(role)
                .build();
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
    	if (refreshToken != null) {
    		Token stored = tokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Token not found"));
    		stored.setUsed(true);
        	stored.setRevoked(true);
        	tokenRepository.save(stored);
    	}
        SecurityContextHolder.clearContext();
    }
    
    public LoginResponse refreshToken(String refreshToken) {
    	// Tạo thời gian hết hạn
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY_MS);
        
        Token stored = tokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Token not found"));

        if (stored.isUsed() || stored.isRevoked() || stored.getExpiration().before(now)) {
        	tokenRepository.deleteAllByUsername(stored.getUsername());
        	throw new AuthorizationDeniedException("Refresh token is invalid");
        }

        // Đánh dấu đã dùng
        stored.setUsed(true);
        tokenRepository.save(stored);
        
        // Tạo mới
        String newAccessToken = jwtUtil.generateAccessToken(stored.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(stored.getUsername());

        tokenRepository.save(new Token(stored.getUsername(), newRefreshToken, expiryDate, false, false));

        // Trả về thông tin mới
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
