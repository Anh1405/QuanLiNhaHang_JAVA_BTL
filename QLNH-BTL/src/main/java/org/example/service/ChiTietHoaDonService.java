package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChiTietHoaDonRequest;
import org.example.entity.ChiTietHoaDon;
import org.example.entity.HoaDon;
import org.example.entity.MonAn;
import org.example.repository.ChiTietHoaDonRepository;
import org.example.repository.HoaDonRepository;
import org.example.repository.MonAnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChiTietHoaDonService {

    private final ChiTietHoaDonRepository repository;
    private final HoaDonRepository hoaDonRepo;
    private final MonAnRepository monAnRepo;

    // 🔹 Lấy tất cả
    public List<ChiTietHoaDon> getAll() {
        return repository.findAll();
    }

    // 🔹 Lấy theo ID
    public ChiTietHoaDon getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hóa đơn"));
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    // 🔥 HÀM ĐỒNG BỘ TỔNG TIỀN (Đã sửa nhận tham số Long)
    private void capNhatLaiTongTienHoaDon(Long idHoaDon) {
        // 1. Nhờ Repository tính tổng tiền mới nhất
        Double tongTienMoi = repository.tinhTongTienCuaHoaDon(idHoaDon);

        // 2. Tìm Hóa đơn và cập nhật
        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hóa Đơn"));

        hoaDon.setTongTien(tongTienMoi);
        hoaDonRepo.save(hoaDon);
    }

    // 🔥 CREATE chuẩn chống Deadlock
    @Transactional
    public ChiTietHoaDon create(ChiTietHoaDonRequest req) {

        // 1. TÌM VÀ KHÓA HÓA ĐƠN NGAY LẬP TỨC (Luồng khác sẽ phải xếp hàng chờ ở dòng này)
        HoaDon hoaDon = hoaDonRepo.findByIdForUpdate(req.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        MonAn monAn = monAnRepo.findById(req.getIdMonAn())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));

        // 2. Lưu chi tiết món ăn
        ChiTietHoaDon ct = new ChiTietHoaDon();
        ct.setHoaDon(hoaDon);
        ct.setMonAn(monAn);
        ct.setSoLuong(req.getSoLuong());
        ct.setGiaTien(monAn.getGiaTien()); // Chỉ lưu đơn giá gốc

        ChiTietHoaDon savedCt = repository.save(ct);
        repository.flush(); // Bắt buộc xả dữ liệu chi tiết xuống DB trước khi tính tiền

        // 3. Cập nhật tổng tiền ngay trong luồng an toàn này
        Double tongTienMoi = repository.tinhTongTienCuaHoaDon(hoaDon.getIdHoaDon());
        hoaDon.setTongTien(tongTienMoi);
        hoaDonRepo.save(hoaDon);

        return savedCt;
    }

    // 🔥 UPDATE chuẩn (Đã tích hợp đồng bộ tiền)
    public ChiTietHoaDon update(Long id, ChiTietHoaDonRequest req) {

        ChiTietHoaDon ct = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết"));

        HoaDon hoaDon = hoaDonRepo.findById(req.getIdHoaDon()).orElseThrow();
        MonAn monAn = monAnRepo.findById(req.getIdMonAn()).orElseThrow();

        ct.setHoaDon(hoaDon);
        ct.setMonAn(monAn);
        ct.setSoLuong(req.getSoLuong());

        // Tính lại tiền chi tiết
        ct.setGiaTien(monAn.getGiaTien() );

        // Ép lưu xuống DB
        ChiTietHoaDon savedCt = repository.saveAndFlush(ct);

        // Gọi hàm đồng bộ tiền cho hóa đơn
        capNhatLaiTongTienHoaDon(hoaDon.getIdHoaDon());

        return savedCt;
    }

    // 🔥 DELETE chống Deadlock
    @Transactional
    public void delete(Long id) {
        ChiTietHoaDon ct = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hóa đơn"));

        Long idHoaDon = ct.getHoaDon().getIdHoaDon();

        // 1. TÌM VÀ KHÓA HÓA ĐƠN
        HoaDon hoaDon = hoaDonRepo.findByIdForUpdate(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hóa Đơn"));

        // 2. Xóa món
        repository.delete(ct);
        repository.flush();

        // 3. Tính lại tiền
        Double tongTienMoi = repository.tinhTongTienCuaHoaDon(idHoaDon);
        hoaDon.setTongTien(tongTienMoi);
        hoaDonRepo.save(hoaDon);
    }
}