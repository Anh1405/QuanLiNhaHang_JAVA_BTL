package org.example.repository;

import org.example.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    // Tìm theo username
    Optional<NguoiDung> findByUsername(String username);
    NguoiDung findByUsernameOrEmail(String username, String email);
    // Tìm theo email
    Optional<NguoiDung> findByEmail(String email);
}