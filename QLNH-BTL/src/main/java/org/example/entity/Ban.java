package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data // Tự tạo Getter, Setter, toString...
@Table(name = "ban")
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ban")
    private Long idBan;
    @Column(name = "so_ban")
    private int soBan;
    @Column(name = "trang_thai")
    private String trangThai; // "Trong", "CoKhach"
}