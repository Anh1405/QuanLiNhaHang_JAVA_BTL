# 🍽️ Hệ Thống Quản Lý Nhà Hàng (Restaurant Management System)

Dự án Bài Tập Lớn (BTL) xây dựng hệ thống phần mềm Quản lý Nhà Hàng, áp dụng kiến trúc 3 lớp (3-tier architecture) kết hợp giao diện Web trực quan, giúp tự động hóa quy trình phục vụ, quản lý hóa đơn và thống kê doanh thu.

**Giảng viên hướng dẫn:** Cô Trần Thị Dung  
**Sinh viên thực hiện:**   
* Nguyễn Lê Quốc Anh (6551071003) .
* Nguyễn Trường Giang (6551071024).
* Phan Nhất Duy (6551071016).

---

## 🚀 Công Nghệ Sử Dụng (Tech Stack)

### Backend
* **Ngôn ngữ:** Java
* **Framework:** Spring Boot
* **Kiến trúc:** 3-Tier Architecture (Controller - Service - Repository)
* **Cơ sở dữ liệu (Database):** SQL / JPA Hibernate

### Frontend
* **Giao diện:** HTML5, CSS3, JavaScript (Vanilla JS)
* **Template:** AdminLTE 3 (Responsive Design)
* **Thư viện giao tiếp:** Axios (gọi API RESTful)
* **Thư viện trực quan hóa:** 
  * `Chart.js` (Vẽ biểu đồ doanh thu)
  * `ExcelJS` & `FileSaver.js` (Xuất báo cáo định dạng `.xlsx`)

---

## 🎯 Các Chức Năng Nổi Bật (Key Features)

Dự án được thiết kế theo dạng **Single Page Application (SPA)**, mang lại trải nghiệm chuyển trang mượt mà không cần tải lại trang. Các module chính bao gồm:

1. **📊 Dashboard (Tổng Quan Kịp Thời)**
   * Theo dõi số lượng đơn hàng, khách hàng, và doanh thu trong ngày.
   * Cập nhật tiến độ KPI (Doanh thu món chính, đồ uống, tỉ lệ lấp đầy bàn) theo thời gian thực.

2. **🪑 Sơ Đồ Bàn (Table Management)**
   * Quản lý trạng thái bàn trực quan: `Sẵn sàng`, `Đã Đặt`, `Có Khách`.
   * Chuyển đổi trạng thái và liên kết trực tiếp với hóa đơn của từng bàn.

3. **🍔 Quản Lý Thực Đơn (Menu Management)**
   * Hiển thị danh sách món ăn theo danh mục.
   * Thêm mới, chỉnh sửa thông tin món ăn, mô tả, và giá tiền.

4. **🧾 Hóa Đơn & Thanh Toán (Billing & Payments)**
   * Tạo đơn mới, thêm/bớt món ăn vào hóa đơn đang chờ.
   * Tự động tính tổng tiền và chuyển trạng thái bàn khi xác nhận thanh toán.

5. **👥 Quản Lý Nhân Sự (Personnel Management)**
   * Liệt kê danh sách tài khoản trong hệ thống.
   * Phân quyền bảo mật chức vụ: `ADMIN` (Quản trị viên) và `USER` (Nhân viên phục vụ/Người dùng).

6. **📈 Báo Cáo Doanh Thu (Revenue Reporting)**
   * Vẽ biểu đồ đường (Line Chart) thể hiện xu hướng doanh thu 7 ngày gần nhất.
   * Bảng thống kê chi tiết số lượng đơn và số tiền theo từng ngày.
   * **Tính năng nâng cao:** Kết xuất dữ liệu báo cáo ra file Excel chuẩn định dạng kế toán.

---

## ⚙️ Hướng Dẫn Cài Đặt (Setup & Run)

### 1. Khởi chạy Backend (Spring Boot)
1. Clone repository này về máy.
2. Mở dự án bằng IDE (IntelliJ IDEA / Eclipse / JetBrains).
3. Cập nhật thông tin kết nối Database trong file `application.properties`:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/qlnh_btl
   spring.datasource.username=root
   spring.datasource.password=your_password
