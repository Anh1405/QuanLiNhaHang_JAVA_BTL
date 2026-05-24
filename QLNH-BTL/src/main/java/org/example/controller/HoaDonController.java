package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.HoaDonRequest;
import org.example.entity.HoaDon;
import org.example.service.HoaDonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoadon")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HoaDonController {

    private final HoaDonService service;

    // 🔹 GET ALL
    @GetMapping
    public List<HoaDon> getAll() {
        return service.getAll();
    }

    // 🔹 GET BY ID
    @GetMapping("/{id}")
    public HoaDon getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // 🔥 CREATE
    @PostMapping
    public HoaDon create(@RequestBody HoaDonRequest req) {
        return service.create(req);
    }

    // 🔥 UPDATE
    @PutMapping("/{id}")
    public HoaDon update(@PathVariable Long id,
                         @RequestBody HoaDonRequest req) {
        return service.update(id, req);
    }

    // 🔥 DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}