
// ==============================
// ORDER HISTORY - MYSQL VERSION (CHỈ CỘNG ĐIỂM KHI ĐÃ THANH TOÁN)
// ==============================

window.loadOrderHistory = async function () {

    const user = JSON.parse(localStorage.getItem("user"));
    if (!user) return;

    const tbody = document.getElementById("orderHistory");
    if (!tbody) return;

    const currentLang = localStorage.getItem("currentLanguage") || "vi";
    const isEnglish = currentLang === "en";

    try {
        // ==============================
        // CALL MYSQL API
        // ==============================
        const res = await fetch("https://restoranqka.up.railway.app/api/hoadon/user/" + user.id);

        if (!res.ok) throw new Error("API error");

        const orders = await res.json();

        console.log("ORDER DATA:", orders);

        // ==============================
        // EMPTY STATE
        // ==============================
        if (!orders || orders.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center">
                        ${isEnglish ? "No orders found" : "Chưa có hóa đơn nào"}
                    </td>
                </tr>
            `;
            return;
        }

        // =======================================================================
        // 🔥 LOGIC LỌC NGHIÊM NGẶT: CHỈ 'DaThanhToan' MỚI ĐƯỢC TÍNH ĐIỂM
        // =======================================================================
        let tongDiemChinhXac = 0;

        orders.forEach(order => {
            // Kiểm tra trạng thái hóa đơn: Phải chính xác là 'DaThanhToan'
            if (order.trangThai === 'DaThanhToan') {
                const items = order.chiTietHoaDons || [];
                items.forEach(item => {
                    // Cộng dồn số lượng món ăn thành điểm tích lũy
                    tongDiemChinhXac += parseInt(item.soLuong || 0, 10);
                });
            }
        });

        // Cập nhật số điểm này vào LocalStorage của trình duyệt
        user.tongDiemTichLuy = tongDiemChinhXac;
        localStorage.setItem("user", JSON.stringify(user));
        console.log("🔥 Đã quét hệ thống! Số điểm tích lũy từ các đơn 'DaThanhToan' là:", tongDiemChinhXac);
        // =======================================================================

        // ==============================
        // TRANSLATION MAP
        // ==============================
        const translationMap = {
            "Salad Nga": "Russian Salad",
            "Khoai tây chiên": "French Fries",
            "Bò lúc lắc": "Shaking Beef",
            "Cơm chiên hải sản": "Seafood Fried Rice",
            "Cà phê sữa": "Vietnamese Iced Coffee with Condensed Milk",
            "Nước ép cam": "Fresh Orange Juice",
            "Bánh tiramisu": "Tiramisu Cake",
            "Kem vani": "Vanilla Ice Cream",
            "Trái cây tổng hợp": "Assorted Fresh Fruits Platter",
            "Lẩu Thái Hải Sản": "Thai Seafood Hotpot",
            "Lẩu Bò Nhúng Dấm": "Beef Hotpot Vinegar Broth",
            "Lẩu Nấm Hải Sản": "Seafood & Mushroom Hotpot",
            "Lẩu Gà Lá É": "Chicken Hotpot with Lemon Basil Leaf",
            "Trà đào cam sả": "Peach Orange Lemongrass Tea",
            "Soda chanh": "Fresh Lime Soda"
        };

        const totalText = isEnglish ? "Total" : "Tổng";
        let html = "";

        // ==============================
        // RENDER ORDERS
        // ==============================
        orders.forEach((order, orderIndex) => {

            const displayOrderId = orderIndex + 1; 
            const items = order.chiTietHoaDons || [];

            items.forEach((item, index) => {

                const mon = item.monAn || {};
                const thanhTien = (item.soLuong || 0) * (item.giaTien || 0);
                let tenMonHienThi = mon.tenMon || "";

                if (isEnglish && translationMap[mon.tenMon]) {
                    tenMonHienThi = translationMap[mon.tenMon];
                }

                html += `
                    <tr>
                        <td>${index === 0 ? "HD-" + displayOrderId : ""}</td>
                        <td>${index + 1}</td>
                        <td>${tenMonHienThi}</td>
                        <td>${item.soLuong}</td>
                        <td>${formatTien(thanhTien, isEnglish)}</td>
                        <td>${formatDate(order.ngayTao)}</td>
                    </tr>
                `;
            });

            // TOTAL ROW
            html += `
                <tr class="table-warning fw-bold">
                    <td>${totalText} HD-${displayOrderId}</td>
                    <td colspan="3"></td>
                    <td>${formatTien(order.tongTien, isEnglish)}</td>
                    <td></td>
                </tr>

                <tr>
                    <td colspan="6" style="height:25px;border:none;background:white;"></td>
                </tr>
            `;
        });

        tbody.innerHTML = html;

    } catch (error) {
        console.error("LOAD ORDER ERROR:", error);

        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center text-danger">
                    ${isEnglish ? "Error loading data" : "Lỗi tải dữ liệu"}
                </td>
            </tr>
        `;
    }
};

// ==============================
// FORMAT MONEY (CẬP NHẬT TỶ GIÁ 26,290 Đ)
// ==============================
function formatTien(tien, isEnglish) {
    if (isEnglish) {
        // Quy đổi từ VND sang USD: Chia cho 26290 và lấy 2 chữ số thập phân
        const tienDo = Number(tien) / 26290;
        return "$" + tienDo.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }
    return Number(tien).toLocaleString("vi-VN") + " đ";
}

// ==============================
// FORMAT DATE
// ==============================
function formatDate(dateStr) {
    if (!dateStr) return "";

    const date = new Date(dateStr);

    return date.toLocaleString("vi-VN");
}

// ==============================
// INIT PAGE
// ==============================
document.addEventListener("DOMContentLoaded", function () {

    const spinner = document.getElementById("spinner");
    if (spinner) spinner.classList.remove("show");

    window.loadOrderHistory();
});
