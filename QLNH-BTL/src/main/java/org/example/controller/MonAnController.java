package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.MonAn;
import org.example.service.MonAnService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MonAnController {

    private final MonAnService service;

    // =========================
    // GET ALL MÓN ĂN
    // =========================
    @GetMapping
    public List<MonAn> getAll() {
        return service.getAll();
    }

    // =========================
    // GET MÓN ĂN THEO ID
    // =========================
    @GetMapping("/{id}")
    public MonAn getById(@PathVariable Integer id) {
        return service.getById(Long.valueOf(id));
    }

    // =========================
    // GET MÓN ĂN THEO DANH MỤC
    // =========================
    @GetMapping("/danhmuc/{idDanhMuc}")
    public List<MonAn> getByDanhMuc(@PathVariable Integer idDanhMuc) {
        return service.getByDanhMuc(Long.valueOf(idDanhMuc));
    }

    // =========================
    // THÊM MÓN ĂN
    // =========================
    @PostMapping
    public MonAn create(@RequestBody MonAn monAn) {
        return service.create(monAn);
    }

    // =========================
    // CẬP NHẬT MÓN ĂN
    // =========================
    @PutMapping("/{id}")
    public MonAn update(@PathVariable Integer id, @RequestBody MonAn monAn) {
        return service.update(Long.valueOf(id), monAn);
    }

    // =========================
    // XÓA MÓN ĂN
    // =========================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(Long.valueOf(id));
    }
}