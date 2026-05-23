package org.example.repository;

import org.example.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {

    // Lấy món ăn theo danh mục
    List<MonAn> findByDanhMuc_IdDanhMuc(Long idDanhMuc);
}