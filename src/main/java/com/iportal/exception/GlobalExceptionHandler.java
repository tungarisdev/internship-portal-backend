package com.iportal.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.UnexpectedTypeException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	// 400 - Lỗi dữ liệu đầu vào
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, "Lỗi nhập liệu: " + errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Lỗi nhập liệu: " + ex.getMessage());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<String> handleUnexpectedType(UnexpectedTypeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Blank input: " + ex.getMessage());
    }

    // 401 - Lỗi xác thực (token, đăng nhập)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Lỗi xác thực: " + ex.getMessage());
    }
    
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> AuthorizationDeniedException(AuthorizationDeniedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Lỗi xác thực: " + ex.getMessage());
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Tài khoản hoặc mật khẩu không chính xác");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleTokenExpired(ExpiredJwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Phiên đăng nhập đã hết hạn");
    }

    // 403 - Lỗi quyền truy cập
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleInternalAuthError(InternalAuthenticationServiceException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // 404 - Không tìm thấy
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Lỗi không tìm thấy: " + ex.getMessage());
    }

    // 405 - Sai method HTTP
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "Phương thức yêu cầu không hỗ trợ");
    }

    // 409 - Conflict từ DB (ví dụ: UNIQUE constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("recruitments.title_UNIQUE")) {
            return buildResponse(HttpStatus.CONFLICT, "Lỗi: Tiêu đề đã tồn tại, vui lòng nhập tiêu đề khác!");
        }
        if (message.contains("users.username_UNIQUE")) {
            return buildResponse(HttpStatus.CONFLICT, "Lỗi: Username đã tồn tại!");
        }
        return buildResponse(HttpStatus.CONFLICT, "Lỗi dữ liệu trùng lặp: " + message);
    }

    // 500 - Lỗi không xác định
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandledExceptions(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + ex.getMessage());
    }

    // Hàm tiện ích để tránh lặp lại
    private ResponseEntity<String> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }
}
