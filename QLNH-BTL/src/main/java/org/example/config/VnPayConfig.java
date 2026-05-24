package org.example.config;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VnPayConfig {
    // Các thông tin cấu hình môi trường Sandbox (Thử nghiệm) của VNPAY
    public static String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_TmnCode = "4M8095E6"; // Điền mã định danh website do VNPAY cấp khi đăng ký test
    public static String vnp_HashSecret = "FOKZDYLYHVDWUPAGXSQTLRIVZUPCHYOH"; // Điền chuỗi bí mật do VNPAY cấp
    public static String vnp_ReturnUrl = "http://localhost:8080/api/vnpay/payment-callback"; // Link VNPAY trả kết quả về sau khi quét QR thành công

    // Thuật toán mã hóa chữ ký số HMAC-SHA512 để VNPAY kiểm tra tính toàn vẹn của số tiền
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                return null;
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    // Hàm lấy IP của thiết bị khách hàng (VNPAY bắt buộc truyền tham số này)
    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "127.0.0.1";
        }
        return ipAdress;
    }
}
