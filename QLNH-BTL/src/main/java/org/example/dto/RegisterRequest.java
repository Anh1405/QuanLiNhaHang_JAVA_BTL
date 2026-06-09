package org.example.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String hoTen;
    private String email;
    private String username;
    private String password;
    private String otp;

}