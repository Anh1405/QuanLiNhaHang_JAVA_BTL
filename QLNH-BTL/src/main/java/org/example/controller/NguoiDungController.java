package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.NguoiDung;
import org.example.repository.NguoiDungRepository;
import org.example.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoidung")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NguoiDungController {

    private final NguoiDungService service;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody NguoiDung nguoiDung) {

        try {

            NguoiDung newUser =
                    service.register(nguoiDung);

            return ResponseEntity.ok(newUser);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String,String> request) {

        String username =
                request.get("username");

        String password =
                request.get("password");

        NguoiDung nguoiDung =
                service.login(
                        username,
                        password
                );

        if(nguoiDung!=null){

            return ResponseEntity
                    .ok(nguoiDung);

        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Tên đăng nhập hoặc mật khẩu không chính xác!");
    }


    // Lấy danh sách người dùng
    @GetMapping
    public List<NguoiDung> layDanhSachNguoiDung(){

        return nguoiDungRepository.findAll();

    }


    // Cập nhật vai trò
    @PutMapping("/{id}/vaitro")
    public NguoiDung capNhatVaiTro(
            @PathVariable Long id,
            @RequestBody Map<String,String> payload){

        NguoiDung nd =
                nguoiDungRepository
                        .findById(id)
                        .orElseThrow(
                                ()->new RuntimeException(
                                        "Không tìm thấy người dùng!"
                                ));

        nd.setVaiTro(
                payload.get("vaiTro")
        );

        return nguoiDungRepository.save(nd);

    }


    // Đổi mật khẩu
    @PutMapping("/{id}/matkhau")
    public ResponseEntity<?> capNhatMatKhau(
            @PathVariable Long id,
            @RequestBody Map<String,String> payload){

        try{

            NguoiDung nd =
                    nguoiDungRepository
                            .findById(id)
                            .orElseThrow(
                                    ()->new RuntimeException(
                                            "Không tìm thấy người dùng!"
                                    ));

            String matKhauMoi =
                    payload.get("password");

            nd.setPassword(
                    matKhauMoi
            );

            nguoiDungRepository.save(
                    nd
            );

            return ResponseEntity.ok(
                    "Đổi mật khẩu thành công!"
            );

        }
        catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());

        }

    }

    @PutMapping("/{id}/capnhat")
    public ResponseEntity<?> capNhatThongTin(
            @PathVariable Long id,
            @RequestBody Map<String,String> body){

        try{

            NguoiDung nd =
                    nguoiDungRepository
                            .findById(id)
                            .orElseThrow();

            String matKhauHienTai =
                    body.get("currentPassword");

            if(!nd.getPassword()
                    .equals(matKhauHienTai)){

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Mật khẩu hiện tại không đúng!");
            }

            nd.setHoTen(
                    body.get("hoTen")
            );

            nd.setEmail(
                    body.get("email")
            );

            nd.setUsername(
                    body.get("username")
            );

            nguoiDungRepository.save(nd);

            return ResponseEntity.ok(nd);

        }catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi cập nhật");

        }

    }
}