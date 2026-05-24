package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.MonAn;
import org.example.repository.MonAnRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonAnService {

    private final MonAnRepository repository;

    // Lấy tất cả món ăn
    public List<MonAn> getAll() {
        return repository.findAll();
    }

    // Lấy món ăn theo ID
    public MonAn getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Lấy món theo danh mục
    public List<MonAn> getByDanhMuc(Long idDanhMuc) {
        return repository.findByDanhMuc_IdDanhMuc(idDanhMuc);
    }

    // Thêm món ăn
    public MonAn create(MonAn monAn) {
        return repository.save(monAn);
    }

    // Cập nhật món ăn
    public MonAn update(Long id, MonAn monAn) {
        monAn.setIdMonAn(id);
        return repository.save(monAn);
    }

    // Xóa món ăn
    public void delete(Long id) {
        repository.deleteById(id);
    }
}