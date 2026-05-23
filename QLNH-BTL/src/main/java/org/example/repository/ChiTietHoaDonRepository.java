package org.example.repository;

import org.example.entity.ChiTietHoaDon;
import org.example.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Long> {

    List<ChiTietHoaDon> findByHoaDon(HoaDon hoaDon);
    @Query("SELECT COALESCE(SUM(c.soLuong * c.giaTien), 0.0) FROM ChiTietHoaDon c WHERE c.hoaDon.idHoaDon = :idHoaDon")
    Double tinhTongTienCuaHoaDon(@Param("idHoaDon") Long idHoaDon);
}