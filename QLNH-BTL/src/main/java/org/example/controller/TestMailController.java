package org.example.controller;

import org.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-mail")
    public String testMail() {

        emailService.sendOtp(
                "ntruonggiang994@gmail.com",
                "123456"
        );

        return "Da gui mail";
    }
}