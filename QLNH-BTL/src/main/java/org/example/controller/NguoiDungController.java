package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ForgotPasswordRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.NguoiDung;
import org.example.repository.NguoiDungRepository;
import org.example.service.EmailService;
import org.example.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.service.OtpService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoidung")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NguoiDungController {

    private final NguoiDungService service;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;


    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
            @RequestParam String email) {

        try {

            System.out.println("EMAIL NHAN: " + email);

            String otp =
                    otpService.generateOtp(email);

            System.out.println("OTP TAO RA: " + otp);

            emailService.sendOtp(
                    email,
                    otp
            );

            System.out.println("DA GUI MAIL");

            return ResponseEntity.ok(
                    "OTP đã gửi tới email"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {

            boolean valid =
                    otpService.verifyOtp(
                            request.getEmail(),
                            request.getOtp()
                    );

            if (!valid) {

                return ResponseEntity
                        .badRequest()
                        .body("OTP không chính xác");
            }

            NguoiDung nd = new NguoiDung();

            nd.setHoTen(
                    request.getHoTen()
            );

            nd.setEmail(
                    request.getEmail()
            );

            nd.setUsername(
                    request.getUsername()
            );

            nd.setPassword(
                    request.getPassword()
            );

            NguoiDung newUser =
                    service.register(nd);

            return ResponseEntity.ok(
                    newUser
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<?> sendOtpForgotPassword(
            @RequestParam String email) {

        NguoiDung nd =
                nguoiDungRepository
                        .findByEmail(email)
                        .orElse(null);

        if (nd == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Email không tồn tại");
        }

        String otp =
                otpService.generateOtp(email);

        emailService.sendOtp(
                email,
                otp
        );

        return ResponseEntity.ok(
                "OTP đã gửi"
        );
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<?> verifyOtpForgot(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String otp = body.get("otp");

        System.out.println("DEBUG EMAIL = " + email);
        System.out.println("DEBUG OTP = " + otp);

        boolean valid = otpService.verifyOtp(email, otp);

        System.out.println("RESULT = " + valid);

        if (!valid) {
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        boolean valid =
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                );

        if (!valid) {

            return ResponseEntity
                    .badRequest()
                    .body("OTP không chính xác");
        }

        NguoiDung nd =
                nguoiDungRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElse(null);

        if (nd == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Email không tồn tại");
        }

        nd.setPassword(
                request.getNewPassword()
        );

        nguoiDungRepository.save(nd);

        return ResponseEntity.ok(
                "Đổi mật khẩu thành công"
        );
    }


    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {

        String username =
                request.get("username");

        String password =
                request.get("password");

        NguoiDung nguoiDung =
                service.login(
                        username,
                        password
                );

        if (nguoiDung != null) {

            return ResponseEntity
                    .ok(nguoiDung);

        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Tên đăng nhập hoặc mật khẩu không chính xác!");
    }


    // Lấy danh sách người dùng
    @GetMapping
    public List<NguoiDung> layDanhSachNguoiDung() {

        return nguoiDungRepository.findAll();

    }


    // Cập nhật vai trò
    @PutMapping("/{id}/vaitro")
    public NguoiDung capNhatVaiTro(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        NguoiDung nd =
                nguoiDungRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Không tìm thấy người dùng!"
                                ));

        nd.setVaiTro(
                payload.get("vaiTro")
        );

        return nguoiDungRepository.save(nd);

    }


    // Đổi mật khẩu
    @PutMapping("/{id}/matkhau")
    public ResponseEntity<?> capNhatMatKhau(
            @PathVariable Long id,
            @RequestBody Map<String,String> payload) {

        try {
            NguoiDung nd = nguoiDungRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

            String matKhauMoi = payload.get("password");

            nd.setPassword(matKhauMoi);
            nguoiDungRepository.save(nd);

            return ResponseEntity.ok("Đổi mật khẩu thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/capnhat")
    public ResponseEntity<?> capNhatThongTin(
            @PathVariable Long id,
            @RequestBody Map<String,String> body){

        try{

            NguoiDung nd =
                    nguoiDungRepository
                            .findById(id)
                            .orElseThrow();

            String matKhauHienTai =
                    body.get("currentPassword");

            if(!nd.getPassword()
                    .equals(matKhauHienTai)){

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Mật khẩu hiện tại không đúng!");
            }

            nd.setHoTen(
                    body.get("hoTen")
            );

            nd.setEmail(
                    body.get("email")
            );

            nd.setUsername(
                    body.get("username")
            );

            nguoiDungRepository.save(nd);

            return ResponseEntity.ok(nd);

        }catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi cập nhật");

        }

    }
    @PutMapping("/matkhau-by-email")
    public ResponseEntity<?> capNhatMatKhauTheoEmail(
            @RequestParam String email,
            @RequestBody Map<String,String> payload) {

        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy email"));

        String matKhauMoi = payload.get("password");

        nd.setPassword(matKhauMoi);
        nguoiDungRepository.save(nd);

        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }
}
