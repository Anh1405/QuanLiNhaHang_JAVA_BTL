package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mon_an")
@Data
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mon_an")
    private Long idMonAn;

    @Column(name = "ten_mon", nullable = false, length = 100)
    private String tenMon;

    @Column(name = "gia_tien", nullable = false)
    private Double giaTien;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    @Column(name = "hinh_anh", columnDefinition = "LONGTEXT")
    private String hinhAnh;

    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    @JsonIgnoreProperties("monAns")
    private DanhMuc danhMuc;
}