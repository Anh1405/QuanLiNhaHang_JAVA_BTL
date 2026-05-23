package org.example.dto;

import lombok.Data;

@Data
public class ChiTietHoaDonRequest {
    private Long idHoaDon;
    private Long idMonAn;
    private Integer soLuong;
}