package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChiTietHoaDonRequest;
import org.example.entity.ChiTietHoaDon;
import org.example.service.ChiTietHoaDonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chitiethoadon")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // để gọi từ web nếu cần
public class ChiTietHoaDonController {

    private final ChiTietHoaDonService service;

    // 🔹 GET ALL
    @GetMapping
    public List<ChiTietHoaDon> getAll() {
        return service.getAll();
    }

    // 🔹 GET BY ID
    @GetMapping("/{id}")
    public ChiTietHoaDon getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // 🔥 CREATE (dùng DTO)
    @PostMapping
    public ChiTietHoaDon create(@RequestBody ChiTietHoaDonRequest req) {
        return service.create(req);
    }

    // 🔥 UPDATE (dùng DTO)
    @PutMapping("/{id}")
    public ChiTietHoaDon update(@PathVariable Long id,
                                @RequestBody ChiTietHoaDonRequest req) {
        return service.update(id, req);
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}