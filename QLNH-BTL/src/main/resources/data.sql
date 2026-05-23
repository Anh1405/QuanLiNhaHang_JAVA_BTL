-- =============================================
-- 1. DANH MỤC MÓN ĂN (Category)
-- =============================================
INSERT IGNORE INTO DanhMuc (idDanhMuc, tenDanhMuc) VALUES (1, 'Khai vị');
INSERT IGNORE INTO DanhMuc (idDanhMuc, tenDanhMuc) VALUES (2, 'Món chính');
INSERT IGNORE INTO DanhMuc (idDanhMuc, tenDanhMuc) VALUES (3, 'Lẩu');
INSERT IGNORE INTO DanhMuc (idDanhMuc, tenDanhMuc) VALUES (4, 'Tráng miệng');
INSERT IGNORE INTO DanhMuc (idDanhMuc, tenDanhMuc) VALUES (5, 'Đồ uống');

-- =============================================
-- 2. DANH SÁCH MÓN ĂN (MonAn)
-- =============================================
-- Khai vị
INSERT IGNORE INTO MonAn (idMonAn, tenMon, giaTien, idDanhMuc) VALUES (1, 'Salad Nga', 45000, 1);
INSERT IGNORE INTO MonAn (idMonAn, tenMon, giaTien, idDanhMuc) VALUES (2, 'Khoai tây chiên', 30000, 1);
-- Món chính
INSERT IGNORE INTO MonAn (idMonAn, tenMon, giaTien, idDanhMuc) VALUES (3, 'Bò lúc lắc', 120000, 2);
INSERT IGNORE INTO MonAn (idMonAn, tenMon, giaTien, idDanhMuc) VALUES (4, 'Cơm chiên hải sản', 85000, 2);
-- Đồ uống
INSERT IGNORE INTO MonAn (idMonAn, tenMon, giaTien, idDanhMuc) VALUES (5, 'Cà phê sữa', 25000, 5);
INSERT IGNORE INTO MonAn (idMonAn, tenMon, giaTien, idDanhMuc) VALUES (6, 'Nước ép cam', 35000, 5);

-- =============================================
-- 3. QUẢN LÝ BÀN (Ban)
-- =============================================
-- Tạo nhanh 10 bàn với các trạng thái khác nhau để test giao diện Swing
INSERT IGNORE INTO ban (idBan, soBan, trangThai) VALUES (1, 1, 'Trong');
INSERT IGNORE INTO ban (idBan, soBan, trangThai) VALUES (2, 2, 'CoKhach');
INSERT IGNORE INTO ban (idBan, soBan, trangThai) VALUES (3, 3, 'Trong');
INSERT IGNORE INTO ban (idBan, soBan, trangThai) VALUES (4, 4, 'DaDat');
INSERT IGNORE INTO ban (idBan, soBan, trangThai) VALUES (5, 5, 'Trong');

-- =============================================
-- 4. NHÂN VIÊN & TÀI KHOẢN (NguoiDung)
-- =============================================
-- Pass giả định: 'admin123' và 'staff123'
INSERT IGNORE INTO NguoiDung (id, hoTen, username, password, vaiTro)
VALUES (1, 'Admin Tổng', 'admin', 'admin123', 'ADMIN');
INSERT IGNORE INTO NguoiDung (id, hoTen, username, password, vaiTro)
VALUES (2, 'Nguyễn Văn Nhân Viên', 'staff01', 'staff123', 'USER');

-- =============================================
-- 5. HÓA ĐƠN (HoaDon)
-- =============================================
-- Hóa đơn cho bàn số 2, nhân viên id=2 thanh toán
INSERT IGNORE INTO HoaDon (idHoaDon, ngayTao, tongTien, idBan, idNguoiDung, trangThai)
VALUES (1, '2024-03-20 10:30:00', 250000, 2, 2, 'DaThanhToan');

INSERT IGNORE INTO HoaDon (idHoaDon, ngayTao, tongTien, idBan, idNguoiDung, trangThai)
VALUES (2, '2024-03-20 11:45:00', 120000, 1, 2, 'DaThanhToan');

-- =============================================
-- 6. CHI TIẾT HÓA ĐƠN (ChiTietHoaDon)
-- =============================================
-- Hóa đơn 1 có: 2 Phở bò (id=1), 2 Cà phê (id=5)
INSERT IGNORE INTO ChiTietHoaDon (idChiTiet, idHoaDon, idMonAn, soLuong, giaTien)
VALUES (1, 1, 1, 2, 50000);
INSERT IGNORE INTO ChiTietHoaDon (idChiTiet, idHoaDon, idMonAn, soLuong, giaTien)
VALUES (2, 1, 5, 2, 25000);