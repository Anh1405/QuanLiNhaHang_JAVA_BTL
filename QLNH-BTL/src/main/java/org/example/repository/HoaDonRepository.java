package org.example.repository;

import jakarta.persistence.LockModeType;
import org.example.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM HoaDon h WHERE h.idHoaDon = :idHoaDon")
    Optional<HoaDon> findByIdForUpdate(@Param("idHoaDon") Long idHoaDon);

    List<HoaDon> findByNguoiDung_Id(Long id);
}