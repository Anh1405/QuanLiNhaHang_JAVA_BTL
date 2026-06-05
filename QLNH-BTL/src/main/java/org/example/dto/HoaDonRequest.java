package org.example.dto;

import lombok.Data;

@Data
public class HoaDonRequest {
    private Long idBan;
    private Double tongTien;
    private String trangThai;
    private Integer soLuongKhach;
    private String tenKhachHang;
    private String soDienThoai;
    private String ghiChu;
    private Long idNguoiDung;
}