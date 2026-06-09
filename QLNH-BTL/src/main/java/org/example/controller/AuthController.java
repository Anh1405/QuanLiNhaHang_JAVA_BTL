package org.example.controller;

import jakarta.mail.MessagingException;
import org.example.repository.NguoiDungRepository;
import org.example.service.EmailService;
import org.example.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NguoiDungRepository repo;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
            @RequestParam String email) throws MessagingException {

        if(repo.findByEmail(email).isPresent()){

            return ResponseEntity.badRequest()
                    .body("Email da ton tai");
        }

        String otp = otpService.generateOtp(email);

        emailService.sendOtp(
                email,
                otp
        );

        return ResponseEntity.ok(
                "Da gui OTP"
        );
    }
}
