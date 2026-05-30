package org.example.dto;

import lombok.Data;

@Data
public class HoaDonRequest {
    private Long idBan;
    private Double tongTien;
    private String trangThai;

    // 🔥 THÊM 3 TRƯỜNG NÀY VÀO ĐỂ HỨNG DỮ LIỆU FRONTEND 🔥
    private String tenKhachHang;
    private String soDienThoai;
    private String ghiChu;

    private Long idNguoiDung;
}