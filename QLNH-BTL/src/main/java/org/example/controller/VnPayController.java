package org.example.controller;

import org.example.config.VnPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
@CrossOrigin(origins = "*") // Giải quyết triệt để lỗi CORS từ bước trước của bạn
public class VnPayController {

    // 1. API Tạo URL Thanh toán cho Frontend
    @GetMapping("/create-payment")
    public ResponseEntity<String> createPayment(
            @RequestParam("amount") long amount,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        // Số tiền gửi lên VNPAY cần nhân thêm 100 theo quy định (Ví dụ: 35000 đ -> 3500000)
        long vnpAmount = amount * 100;

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis()); // Mã tham chiếu hóa đơn tạm thời (Unique)
        String vnp_IpAddr = VnPayConfig.getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Tạo chuỗi thời gian khởi tạo giao dịch
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Đóng gói tham số, sắp xếp thứ tự Alphabet để tạo chữ ký
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Xây dựng chuỗi băm dữ liệu
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Xây dựng chuỗi truy vấn URL
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Tiến hành băm chữ ký số HMAC-SHA512
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        // Đường link hoàn chỉnh chứa mã QR ảo và số tiền chính xác
        String paymentUrl = VnPayConfig.vnp_Url + "?" + queryUrl;

        // Trả chuỗi URL về cho Axios nhận diện
        return ResponseEntity.status(HttpStatus.OK).body(paymentUrl);
    }

    // 2. API Nhận phản hồi kết quả từ cổng VNPAY (VNPAY Callback)
    @GetMapping("/payment-callback")
    public ResponseEntity<String> paymentCallback(
            @RequestParam Map<String, String> queryParams) {

        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String orderInfo = queryParams.get("vnp_OrderInfo"); // Ví dụ: "Thanh toan dat ban hoadon HD-32"

        // Kiểm tra xem giao dịch thành công hay không (vnp_ResponseCode == "00" là thành công)
        if ("00".equals(vnp_ResponseCode)) {

            // 👉 Tách chuỗi lấy ID Hóa đơn thật từ text nội dung orderInfo để cập nhật database
            // (Bạn nên viết logic trích xuất id hoặc dựa vào mã vnp_TxnRef tùy thiết kế hệ thống)
            System.out.println("Giao dịch thành công cho đơn: " + orderInfo);

            /* TODO: THỰC HIỆN LOGIC CẬP NHẬT DATABASE Ở ĐÂY:
               1. Tìm hóa đơn tương ứng với nội dung orderInfo.
               2. Chuyển trạng thái hóa đơn từ 'ChoThanhToan' -> 'DaThanhToan'.
            */

            // Chuyển hướng người dùng về một trang thông báo thành công đẹp đẽ trên giao diện của bạn
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:5500/payment-success.html")
                    .body("Thanh toán thành công!");
        } else {
            // Trường hợp giao dịch thất bại hoặc người dùng hủy quét QR
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:5500/payment-failed.html")
                    .body("Thanh toán thất bại!");
        }
    }
}
