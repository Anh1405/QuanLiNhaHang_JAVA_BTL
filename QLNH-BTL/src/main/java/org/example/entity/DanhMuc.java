package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "danh_muc")
@Data
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDanhMuc;

    @Column(name = "ten_danh_muc")
    private String tenDanhMuc;
}