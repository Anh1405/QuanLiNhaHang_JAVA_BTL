package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.DanhMucRequest;
import org.example.entity.DanhMuc;
import org.example.repository.DanhMucRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhMucService {

    private final DanhMucRepository repository;

    // 🔹 GET ALL
    public List<DanhMuc> getAll() {
        return repository.findAll();
    }

    // 🔹 GET BY ID
    public DanhMuc getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
    }

    // 🔥 CREATE
    public DanhMuc create(DanhMucRequest req) {
        DanhMuc dm = new DanhMuc();
        dm.setTenDanhMuc(req.getTenDanhMuc());
        return repository.save(dm);
    }

    // 🔥 UPDATE
    public DanhMuc update(Long id, DanhMucRequest req) {
        DanhMuc dm = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy"));

        dm.setTenDanhMuc(req.getTenDanhMuc());
        return repository.save(dm);
    }

    // 🔹 DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}