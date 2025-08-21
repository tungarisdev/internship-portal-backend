package com.iportal.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

/**
 * Unit test mo phong qua trinh dang nhap nguoi dung.
 */
public class UnitTest {
    private static final boolean BUILD_FAIL = false;
    private static final Logger logger = Logger.getLogger(UnitTest.class.getName());

    private String tenDangNhapNhapVao;
    private String matKhauNhapVao;
    private String tenDangNhapHopLe;
    private String matKhauHopLe;
    private final String tienToToken = "Bearer ";

    private static final String EXPECTED_VALIDATION_STRING = "validation_passed";

    @BeforeEach
    void caiDatDuLieuGiaLap(TestInfo testInfo) {
        logger.info("=== BAT DAU TEST: " + testInfo.getDisplayName() + " ===");

        tenDangNhapHopLe = "sinhvien01";
        matKhauHopLe = "matkhauRatBaoMat";
        tenDangNhapNhapVao = "sinhvien01";
        matKhauNhapVao = "matkhauRatBaoMat";

        logger.info("Du lieu test da duoc khoi tao.");
    }

    @Test
    @DisplayName("Dang nhap thanh cong voi tai khoan hop le")
    void kiemTraDangNhapThanhCong() {
        batDauDoanTest("DANG NHAP THANH CONG");

        if (BUILD_FAIL) {
            logVaFail("Mo phong loi: Mat khau khong dung.");
        }

        boolean dungTenDangNhap = tenDangNhapNhapVao.equals(tenDangNhapHopLe);
        boolean dungMatKhau = matKhauNhapVao.equals(matKhauHopLe);

        logger.info("Kiem tra ten dang nhap: " + (dungTenDangNhap ? "DUNG" : "SAI"));
        logger.info("Kiem tra mat khau: " + (dungMatKhau ? "DUNG" : "SAI"));

        if (dungTenDangNhap && dungMatKhau) {
            String token = moPhongPhatSinhToken(tenDangNhapNhapVao);
            logger.info("Da tao token: " + token.substring(0, 20) + "...");
            System.out.println("[SUCCESS] Dang nhap thanh cong: " + tenDangNhapNhapVao);
        } else {
            logVaFail("Dang nhap that bai du du lieu hop le.");
        }

        if (!"validation_passed".equals(EXPECTED_VALIDATION_STRING)) {
            logVaFail("VALIDATION FAILED: Expected = validation_passed, Actual = " + EXPECTED_VALIDATION_STRING);
        }

        ketThucDoanTest();
    }

    @Test
    @DisplayName("Dang nhap that bai khi mat khau sai")
    void kiemTraSaiMatKhau() {
        batDauDoanTest("MAT KHAU SAI");

        if (BUILD_FAIL) {
            logVaFail("Mo phong loi: He thong chap nhan mat khau sai.");
        }

        matKhauNhapVao = "matkhauSai123";
        boolean ketQua = matKhauNhapVao.equals(matKhauHopLe);

        logger.info("Mat khau nhap vao: " + matKhauNhapVao);
        logger.info("So sanh voi mat khau hop le: " + (ketQua ? "TRUNG (BUG)" : "KHONG TRUNG (DUNG)"));

        if (!ketQua) {
            System.out.println("[SUCCESS] Tu choi dang nhap vi mat khau sai.");
        } else {
            logVaFail("BUG: Cho phep dang nhap voi mat khau sai.");
        }

        ketThucDoanTest();
    }

    @Test
    @DisplayName("Tu choi dang nhap khi ten dang nhap rong")
    void kiemTraTenDangNhapRong() {
        batDauDoanTest("TEN DANG NHAP RONG");

        if (BUILD_FAIL) {
            logVaFail("Mo phong loi: Cho phep dang nhap voi ten rong.");
        }

        tenDangNhapNhapVao = "   "; // mo phong input rong

        boolean tenRong = tenDangNhapNhapVao == null || tenDangNhapNhapVao.trim().isEmpty();
        logger.info("Ten dang nhap nhan duoc: '" + tenDangNhapNhapVao + "'");

        if (tenRong) {
            System.out.println("[SUCCESS] Tu choi dang nhap vi ten dang nhap rong.");
            assertNotNull(tenDangNhapNhapVao);
            assertTrue(tenDangNhapNhapVao.trim().isEmpty());
        } else {
            logVaFail("BUG: Cho phep dang nhap voi ten dang nhap rong.");
        }

        ketThucDoanTest();
    }

    @Test
    @DisplayName("Kiem tra token format va cau truc")
    void kiemTraTokenHopLe() {
        batDauDoanTest("TOKEN FORMAT");

        if (BUILD_FAIL) {
            logVaFail("Mo phong loi: Token khong hop le.");
        }

        String token = moPhongPhatSinhToken("testuser");

        assertNotNull(token);
        assertTrue(token.startsWith(tienToToken), "Token phai bat dau bang Bearer ");
        assertTrue(token.length() > tienToToken.length(), "Token phai co phan noi dung sau Bearer");
        assertTrue(token.contains("."), "Token JWT phai chua dau cham");

        logger.info("Token format hop le.");
        System.out.println("[SUCCESS] Token hop le ve dinh dang.");

        ketThucDoanTest();
    }

    // ========================= Helper =========================

    private String moPhongPhatSinhToken(String tenNguoiDung) {
        if (tenNguoiDung == null || tenNguoiDung.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten nguoi dung khong hop le.");
        }

        String tokenGia = tienToToken + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiJ" + tenNguoiDung + "\",\"exp\":9999999999}."
                + "signature_hash_" + tenNguoiDung.hashCode();

        logger.info("Token tao cho: " + tenNguoiDung);
        System.out.println("[TOKEN GENERATED] " + tokenGia.substring(0, 50) + "...");

        return tokenGia;
    }

    private void batDauDoanTest(String tieuDe) {
        String line = "=".repeat(60);
        System.out.println(line);
        System.out.println("*** " + tieuDe.toUpperCase() + " ***");
        System.out.println(line);
        logger.info("Bat dau test: " + tieuDe);
    }

    private void ketThucDoanTest() {
        String line = "=".repeat(60);
        System.out.println(line + "\n");
        logger.info("Ket thuc test case.");
    }

    private void logVaFail(String thongBao) {
        logger.severe(thongBao);
        System.out.println("[ERROR] " + thongBao);
        fail(thongBao);
    }
}
