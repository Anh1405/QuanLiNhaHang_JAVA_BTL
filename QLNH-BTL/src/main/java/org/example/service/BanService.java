package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Ban;
import org.example.repository.banRepository; // Giả sử bạn đã có Repository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BanService {

    private final banRepository repository;

    @Transactional
    public void updateStatus(int soBan, String newStatus) { // Đã bỏ static
        // Tìm bàn trong database theo số bàn
        Optional<Ban> banOptional = repository.findBySoBan(soBan);

        if (banOptional.isPresent()) {
            Ban ban = banOptional.get();
            ban.setTrangThai(newStatus);
            repository.save(ban);
        } else {
            throw new RuntimeException("Không tìm thấy bàn số: " + soBan);
        }
    }
}