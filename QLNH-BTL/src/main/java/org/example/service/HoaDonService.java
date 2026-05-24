package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.HoaDonRequest;
import org.example.entity.Ban;
import org.example.entity.HoaDon;
import org.example.repository.HoaDonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository repository;

    // 🔹 GET ALL
    public List<HoaDon> getAll() {
        return repository.findAll();
    }

    // 🔹 GET BY ID
    public HoaDon getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
    }
    

    // 🔥 UPDATE
    public HoaDon update(Long id, HoaDonRequest req) {

        HoaDon hoaDon = getById(id);
        hoaDon.setTrangThai(req.getTrangThai());

        return repository.save(hoaDon);
    }

    // 🔥 DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
    public HoaDon create(HoaDonRequest req) {
        HoaDon hoaDon = new HoaDon();

        // 1. Copy thông tin khách hàng từ Request sang Entity
        hoaDon.setTenKhachHang(req.getTenKhachHang());
        hoaDon.setSoDienThoai(req.getSoDienThoai());
        hoaDon.setGhiChu(req.getGhiChu());

        // 2. Set các thông tin khác
        hoaDon.setTongTien(req.getTongTien());
        hoaDon.setTrangThai(req.getTrangThai() != null ? req.getTrangThai() : "ChoThanhToan");

        // 3. Xử lý map idBan thành đối tượng Ban (Rất quan trọng để cột id_ban trong DB không bị null)
        if (req.getIdBan() != null) {
            Ban ban = new Ban();
            ban.setIdBan(req.getIdBan()); // Khởi tạo 1 đối tượng Ban chỉ cần ID là Spring Boot sẽ tự hiểu để lưu khóa ngoại
            hoaDon.setBan(ban);
        }

        // 4. Lưu vào Database bằng biến 'repository'
        return repository.save(hoaDon);
    }
}