# Internship Portal Backend

## Giới thiệu
Internship Portal Backend là phần backend của nền tảng hỗ trợ sinh viên tìm kiếm và quản lý cơ hội thực tập, đồng thời cho phép nhà tuyển dụng đăng bài tuyển dụng và quản lý ứng viên. Dự án được phát triển bằng **Java** với **Spring Boot**, cung cấp các API RESTful để giao tiếp với frontend và quản lý dữ liệu thông qua cơ sở dữ liệu quan hệ hoặc NoSQL (tùy cấu hình).

Mục tiêu chính:
- Quản lý người dùng (sinh viên, nhà tuyển dụng, quản trị viên).
- Tạo, quản lý và theo dõi các cơ hội thực tập.
- Đảm bảo bảo mật với xác thực JWT và phân quyền theo vai trò.

---

## Công nghệ sử dụng
- **Ngôn ngữ lập trình:** Java
- **Framework:** Spring Boot
- **Bảo mật:** Spring Security, JWT (JSON Web Token)
- **Cơ sở dữ liệu:** MySQL / PostgreSQL / MongoDB (tùy cấu hình)
- **Mapping dữ liệu:** ModelMapper
- **Quản lý phiên bản:** Git
- **Các thư viện khác:** Jackson (xử lý JSON, bao gồm LocalDateDeserializer và StringTrimToNullDeserializer)

---

## Tính năng chính

### 1. Quản lý người dùng
- Đăng nhập với xác thực JWT (`AuthController`, `LoginRequest`, `LoginResponse`).
- Quản lý vai trò (`RoleController`, `RoleService`).
- Cập nhật thông tin người dùng (`UserController`, `ChangePasswordRequest`).

### 2. Quản lý sinh viên
- Tạo và cập nhật hồ sơ sinh viên (`StudentController`, `UpdateStudentRequest`).
- Liệt kê danh sách sinh viên (`ListStudentResponse`).

### 3. Quản lý nhà tuyển dụng
- Tạo và cập nhật thông tin nhà tuyển dụng (`EmployerController`, `UpdateEmployerRequest`).
- Liệt kê danh sách nhà tuyển dụng (`ListEmployerResponse`).

### 4. Quản lý cơ hội thực tập
- Tạo, cập nhật và xem danh sách bài đăng tuyển dụng (`RecruitmentController`, `CreateRecruitmentRequest`, `ListRecruitmentResponse`).

### 5. Quản lý ứng tuyển
- Sinh viên ứng tuyển vào các vị trí thực tập (`StudentEmployerController`, `ApplyRequest`).
- Theo dõi trạng thái ứng tuyển và phản hồi (`ListApplyedResponse`, `ReplyResponse`).

### 6. Bảo mật
- Xác thực và phân quyền dựa trên JWT (`JwtFilter`, `JwtUtil`, `SecurityConfig`).
- Xử lý lỗi toàn cục (`GlobalExceptionHandler`).

---

## Cấu trúc thư mục
```
/src/main/java/com/iportal
│
├─ Main.java                # Điểm khởi chạy ứng dụng Spring Boot
├─ /config                  # Cấu hình ứng dụng (Spring Security, ModelMapper)
├─ /controller              # Các API endpoint
├─ /dto                     # Data Transfer Object
├─ /entity                  # Thực thể cơ sở dữ liệu
├─ /exception               # Xử lý ngoại lệ toàn cục
├─ /repository              # Giao tiếp cơ sở dữ liệu
├─ /security                # Bảo mật và xác thực JWT
├─ /service                 # Logic nghiệp vụ
└─ /utils                   # Các tiện ích (deserializer JSON, v.v.)
```

---

## Cài đặt và chạy dự án
1. Clone repository:
```bash
git clone https://github.com/username/internship-portal-backend.git
```

2. Cấu hình cơ sở dữ liệu trong `application.properties`.

3. Chạy ứng dụng bằng Maven:
```bash
mvn spring-boot:run
```

4. API sẽ sẵn sàng tại `http://localhost:8080/`.

---

## Liên hệ
- Người phát triển: [Tên bạn / TungarisDev]
- Email: [your-email@example.com]

