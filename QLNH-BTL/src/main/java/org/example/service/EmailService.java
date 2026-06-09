package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {

        try {

            String subject = "Restoran - Xác thực tài khoản của bạn";

            String content =
                    "<div style='font-family:Arial, sans-serif; background:#f4f4f4; padding:40px 0;'>"

                            + "<div style='max-width:520px; margin:auto; background:#ffffff; border-radius:14px; overflow:hidden; "
                            + "box-shadow:0 10px 25px rgba(0,0,0,0.1);'>"

                            // HEADER
                            + "<div style='background:linear-gradient(135deg,#d4a762,#b88a4a); padding:25px; text-align:center;'>"
                            + "<h1 style='color:white; margin:0; letter-spacing:3px;'>RESTORAN</h1>"
                            + "<p style='color:#fff; margin:5px 0 0; font-size:13px;'>Trải nghiệm ẩm thực đẳng cấp</p>"
                            + "</div>"

                            // BODY
                            + "<div style='padding:30px;'>"

                            + "<h2 style='color:#333; margin-bottom:10px;'>Xác thực tài khoản</h2>"

                            + "<p style='color:#666; font-size:15px; line-height:1.6;'>"
                            + "Xin chào quý khách, chúng tôi nhận được yêu cầu xác thực từ hệ thống <b>Restoran</b>. "
                            + "Vui lòng sử dụng mã OTP bên dưới để hoàn tất thao tác của bạn:</p>"

                            // OTP BOX
                            + "<div style='margin:30px 0; text-align:center;'>"
                            + "<div style='display:inline-block; padding:18px 35px; "
                            + "font-size:30px; letter-spacing:8px; font-weight:bold; "
                            + "color:#d4a762; border:2px dashed #d4a762; border-radius:12px; background:#fffaf3;'>"
                            + otp
                            + "</div>"
                            + "</div>"

                            // WARNING
                            + "<div style='background:#fff3cd; border-left:5px solid #d4a762; padding:10px 15px; "
                            + "font-size:13px; color:#856404; border-radius:6px;'>"
                            + "⏳ Mã OTP có hiệu lực trong <b>5 phút</b>. Vui lòng không chia sẻ cho bất kỳ ai."
                            + "</div>"

                            + "<p style='margin-top:20px; font-size:13px; color:#999; text-align:center;'>"
                            + "Nếu bạn không thực hiện yêu cầu này, bạn có thể bỏ qua email.</p>"

                            + "</div>"

                            // FOOTER
                            + "<div style='background:#111; color:#aaa; text-align:center; padding:15px; font-size:12px;'>"
                            + "© 2026 Restoran • Nhà hàng ẩm thực cao cấp"
                            + "</div>"

                            + "</div></div>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom("ntruonggiang180606@gmail.com"); // rất quan trọng
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message); // 🔥 ĐÂY MỚI LÀ CHỖ GỬI

        } catch (Exception e) {
            throw new RuntimeException("Lỗi gửi OTP email", e);
        }
    }
}