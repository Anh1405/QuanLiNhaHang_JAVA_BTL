package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.DanhMucRequest;
import org.example.entity.DanhMuc;
import org.example.service.DanhMucService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danhmuc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DanhMucController {

    private final DanhMucService service;

    // 🔹 GET ALL
    @GetMapping
    public List<DanhMuc> getAll() {
        return service.getAll();
    }

    // 🔹 GET BY ID
    @GetMapping("/{id}")
    public DanhMuc getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // 🔥 CREATE
    @PostMapping
    public DanhMuc create(@RequestBody DanhMucRequest req) {
        return service.create(req);
    }

    // 🔥 UPDATE
    @PutMapping("/{id}")
    public DanhMuc update(@PathVariable Long id,
                          @RequestBody DanhMucRequest req) {
        return service.update(id, req);
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}