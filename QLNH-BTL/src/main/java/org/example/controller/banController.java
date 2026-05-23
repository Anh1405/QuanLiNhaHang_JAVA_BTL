package org.example.controller;

import org.example.repository.banRepository;
import org.example.entity.Ban;
import org.example.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ban")
@CrossOrigin(origins = "*")
public class banController {

    @Autowired
    private banRepository banRepository;

    // BƯỚC 1: Thêm dòng này để tiêm Service vào Controller
    @Autowired
    private BanService banService;

    @GetMapping
    public List<Ban> findAll(){
        return banRepository.findAll();
    }

    @PostMapping
    public Ban addBan(@RequestBody Ban ban){
        return banRepository.save(ban);
    }

    @PutMapping("/{soBan}/status")
    public String updateStatus(@PathVariable int soBan, @RequestBody Map<String, String> body) {
        String trangThaiMoi = body.get("trangThai");

        // BƯỚC 2: Gọi qua biến instance 'banService' (chữ b thường)
        banService.updateStatus(soBan, trangThaiMoi);

        return "Thành công";
    }@PutMapping("/{id}")
    public ResponseEntity<Ban> updateBan(@PathVariable Long id, @RequestBody Ban banDetails) {
        Optional<Ban> optionalBan = banRepository.findById(id);

        if (optionalBan.isPresent()) {
            Ban banUpdate = optionalBan.get();
            // Cập nhật trạng thái mới
            banUpdate.setTrangThai(banDetails.getTrangThai());

            // Nếu entity của bạn yêu cầu các trường khác (như username, password...)
            // thì cập nhật thêm ở đây nếu cần thiết, hoặc giữ nguyên giá trị cũ.

            banRepository.save(banUpdate);
            return ResponseEntity.ok(banUpdate);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}