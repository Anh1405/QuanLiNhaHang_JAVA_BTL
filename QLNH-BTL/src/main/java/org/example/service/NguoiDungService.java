package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.NguoiDung;
import org.example.repository.NguoiDungRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {

    private final NguoiDungRepository repository;

    public NguoiDung register(NguoiDung nguoiDung) {
        // Kiểm tra trùng username
        Optional<NguoiDung> existingUser = repository.findByUsername(nguoiDung.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // Kiểm tra trùng email
        Optional<NguoiDung> existingEmail = repository.findByEmail(nguoiDung.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email này đã được sử dụng!");
        }

        // Set quyền mặc định cho khách hàng đăng ký trên web
        if (nguoiDung.getVaiTro() == null) {
            nguoiDung.setVaiTro("USER");
        }

        return repository.save(nguoiDung);
    }

    public NguoiDung login(String taiKhoanNhapVao, String matKhau) {
        // Truyền giá trị khách nhập vào CẢ 2 chỗ (username và email)
        // Spring Boot sẽ tự hiểu là: SELECT * FROM NguoiDung WHERE username = ? OR email = ?
        NguoiDung user = repository.findByUsernameOrEmail(taiKhoanNhapVao, taiKhoanNhapVao);

        // Nếu tìm thấy tài khoản và mật khẩu khớp nhau thì cho đăng nhập thành công
        if (user != null && user.getPassword().equals(matKhau)) {
            return user;
        }

        return null; // Sai tài khoản hoặc mật khẩu
    }
}