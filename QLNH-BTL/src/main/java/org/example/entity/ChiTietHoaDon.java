package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "chi_tiet_hoa_don")
@Data
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chi_tiet")
    private Long idChiTiet;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    @JsonIgnore
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_mon_an")
    private MonAn monAn;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_tien")
    private Double giaTien;
    @JsonProperty("idHoaDon")
    public Long getIdHoaDonCuaChiTiet() {
        if (this.hoaDon != null) {
            // Lưu ý: Nếu trong class HoaDon biến ID tên là "id" thì đổi thành getId() nhé
            return this.hoaDon.getIdHoaDon();
        }
        return null;
    }
}