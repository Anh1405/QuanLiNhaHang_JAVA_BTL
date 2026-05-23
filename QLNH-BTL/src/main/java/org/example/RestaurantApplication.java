package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);

        System.out.println("===============================================");
        System.out.println("🚀 SERVER QUẢN LÝ NHÀ HÀNG ĐÃ SẴN SÀNG!");

        // 🔥 Các API chính
        System.out.println("🔗 API Bàn             : http://localhost:8080/api/ban");
        System.out.println("🔗 API Danh mục        : http://localhost:8080/api/danhmuc");
        System.out.println("🔗 API Món ăn          : http://localhost:8080/api/monan");
        System.out.println("🔗 API Hóa đơn         : http://localhost:8080/api/hoadon");
        System.out.println("🔗 API Chi tiết HĐ     : http://localhost:8080/api/chitiethoadon");

        // 🔥 API Người dùng (Xác thực)
        System.out.println("🔗 API Đăng ký         : http://localhost:8080/api/nguoidung/register");
        System.out.println("🔗 API Đăng nhập       : http://localhost:8080/api/nguoidung/login");
        System.out.println("===============================================");
    }
}