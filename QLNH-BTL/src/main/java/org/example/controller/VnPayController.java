package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.entity.HoaDon;
import org.example.entity.Ban;
import org.example.entity.NguoiDung;
import org.example.repository.HoaDonRepository;
import org.example.config.VnPayConfig;
import org.example.repository.banRepository; // Hãy chắc chắn file Repo của bạn tên là banRepository hoặc BanRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
@CrossOrigin(origins = "*")
public class VnPayController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // === BỔ SUNG: Inject đối tượng Repository quản lý Bàn vào để dùng chung ===
    @Autowired
    private banRepository banRepository;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> requestData, HttpServletRequest request) throws UnsupportedEncodingException {

        HoaDon hoaDon = new HoaDon();
        hoaDon.setTenKhachHang(requestData.get("tenKhachHang").toString());
        hoaDon.setSoDienThoai(requestData.get("soDienThoai").toString());
        hoaDon.setSoLuongKhach(Integer.parseInt(requestData.get("soLuongKhach").toString()));
        hoaDon.setGhiChu(requestData.get("ghiChu") != null ? requestData.get("ghiChu").toString() : "");

        Double tongTien = Double.parseDouble(requestData.get("tongTien").toString());
        hoaDon.setTongTien(tongTien);

        // ĐỒNG BỘ SQL: Chuyển từ "CHO_THANH_TOAN" thành "ChoThanhToan"
        hoaDon.setTrangThai("ChoThanhToan");
        hoaDon.setNgayTao(LocalDateTime.now());

        if (requestData.get("idBan") != null) {
            Ban ban = new Ban();
            ban.setIdBan(Long.parseLong(requestData.get("idBan").toString()));
            hoaDon.setBan(ban);
        }

        if (requestData.get("idNguoiDung") != null) {
            NguoiDung nguoiDung = new NguoiDung();
            nguoiDung.setId(Long.parseLong(requestData.get("idNguoiDung").toString()));
            hoaDon.setNguoiDung(nguoiDung);
        }

        hoaDon = hoaDonRepository.save(hoaDon);

        long amount = (long) (hoaDon.getTongTien() * 100);
        String vnp_TxnRef = String.valueOf(hoaDon.getIdHoaDon());

        String vnp_OrderInfo = "Thanh toan don dat ban cua sdt " + hoaDon.getSoDienThoai() + " ma hoa don: " + hoaDon.getIdHoaDon();
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", VnPayConfig.getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        Map<String, Object> result = new HashMap<>();
        result.put("paymentUrl", paymentUrl);
        result.put("idHoaDon", hoaDon.getIdHoaDon());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/payment-callback")
    public ResponseEntity<?> paymentCallback(@RequestParam Map<String, String> fields) {
        String responseCode = fields.get("vnp_ResponseCode");
        String idHoaDonString = fields.get("vnp_TxnRef");
        Long idHoaDon = Long.parseLong(idHoaDonString);

        HoaDon hoaDonUpdate = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn mã số: " + idHoaDon));

        if ("00".equals(responseCode)) {
            // Cập nhật trạng thái hóa đơn thành 'DaThanhToan' khớp SQL của bạn
            hoaDonUpdate.setTrangThai("DaThanhToan");
            hoaDonRepository.save(hoaDonUpdate);

            // Tự động cập nhật chuyển đổi trạng thái bàn ăn
            Ban banAn = hoaDonUpdate.getBan();
            if (banAn != null) {
                // Đảm bảo lấy ID bàn chính xác từ mối quan hệ JPA Entity
                Long idBanChinhThuc = banAn.getIdBan();

                // Tìm kiếm dữ liệu bàn thật từ DB lên trước khi chỉnh sửa trạng thái
                Optional<Ban> banRealOpt = banRepository.findById(idBanChinhThuc);
                if (banRealOpt.isPresent()) {
                    Ban banReal = banRealOpt.get();
                    banReal.setTrangThai("CoKhach");

                    // Gọi qua đối tượng Bean 'banRepository' đã được @Autowired ở trên đầu trang
                    banRepository.save(banReal);
                }
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Hóa đơn đã chuyển sang DaThanhToan và bàn đã chuyển sang CoKhach"
            ));

        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "fail",
                    "message", "Giao dịch không thành công. Hóa đơn vẫn giữ trạng thái ChoThanhToan"
            ));
        }
    }
}