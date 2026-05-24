// Đóng gói logic vào một hàm toàn cục để file language.js có thể gọi lại khi đổi ngôn ngữ
window.loadOrderHistory = function() {
    const user = JSON.parse(localStorage.getItem("user"));
    const allOrders = JSON.parse(localStorage.getItem("orders")) || [];

    // Kiểm tra bảo hiểm nếu chưa đăng nhập
    if (!user) return;

    // Chỉ lấy hóa đơn của tài khoản đang đăng nhập
    const orders = allOrders.filter(order => order.idNguoiDung === user.id);
    const tbody = document.getElementById("orderHistory");
    if (!tbody) return; // Bảo hiểm nếu không có thẻ trong trang

    // 1. Kiểm tra ngôn ngữ hệ thống hiện tại
    const currentLang = localStorage.getItem("currentLanguage") || "vi";

    // 2. Dịch thông báo khi không có đơn hàng
    if (orders.length === 0) {
        const noOrderText = currentLang === "en" ? "No orders found" : "Chưa có đơn nào";
        tbody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center">
                ${noOrderText}
            </td>
        </tr>
        `;
        return;
    }

    // Bộ từ điển dịch nhanh tên món ăn đồng bộ từ trang chủ
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
        "Pepsi": "Pepsi",
        "Soup bí đỏ": "Pumpkin Soup",
        "Gỏi cuốn tôm thịt": "Fresh Spring Rolls with Pork & Shrimp",
        "Mì Ý sốt bò bằm": "Spaghetti Bolognese",
        "Lẩu Tứ Xuyên": "Sichuan Hotpot",
        "Lẩu Kim Chi Bò Mỹ": "Kimchi Hotpot with American Beef",
        "Lẩu Cá Diêu Hồng": "Red Tilapia Fish Hotpot",
        "Lẩu Hải Sản Tomyum": "Tomyum Seafood Hotpot",
        "Lẩu Gà Tiềm Ớt Hiểm": "Chicken Hotpot with Wild Chili Peppers",
        "Soda chanh": "Fresh Lime Soda",
        "Chocolate đá xay": "Chocolate Ice Blended",
        "Blue Ocean": "Blue Ocean Mocktail",
        "Bia Tiger": "Tiger Beer",
        "Strongbow Dâu": "Strongbow Red Berries Cider",
        "Soju Hàn Quốc": "Korean Soju",
        "Whisky Chivas 12": "Chivas Regal 12 Whisky",
        "Salad cá ngừ": "Tuna Salad",
        "Phô mai que": "Cheese Sticks",
        "Tôm chiên hoàng kim": "Golden Salted Egg Fried Shrimps",
        "Bánh mì bơ tỏi": "Garlic Bread",
        "Đậu hũ chiên sả ớt": "Fried Tofu with Lemongrass & Chili",
        "Bạch tuộc viên takoyaki": "Takoyaki Octopus Balls",
        "Sườn Nướng BBQ": "BBQ Pork Ribs",
        "Bò Steak Sốt Tiêu Đen": "Beef Steak with Black Pepper Sauce",
        "Gà Nướng Mật Ong": "Honey Glazed Grilled Chicken",
        "Bò Nướng Đá": "Hot Stone Grilled Beef",
        "Tôm Hùm Bỏ Lò Phô Mai": "Baked Lobster with Cheese",
        "Mực Nướng Sa Tế": "Grilled Squid with Satay Sauce",
        "Sườn Cừu Nướng": "Grilled Lamb Chops",
        "Panna Cotta Việt Quất": "Blueberry Panna Cotta",
        "Bánh Mousse Chocolate": "Chocolate Mousse Cake",
        "Chè Khúc Bạch": "Khuc Bach Sweet Soup",
        "Bánh Flan Caramel": "Caramel Flan",
        "Pudding Trứng": "Egg Pudding",
        "Crepe Chuối Chocolate": "Banana Chocolate Crepe"
    };

    const totalText = currentLang === "en" ? "Total" : "Tổng";
    let html = "";

    // 🌟 Đổi tên index vòng lặp ngoài thành orderIdx để tạo số thứ tự hóa đơn liên tiếp
    orders.forEach((order, orderIdx) => {
        // Tạo chuỗi số thứ tự hóa đơn (Bắt đầu từ 1, 2, 3...)
        const displayOrderId = orderIdx + 1;

        order.danhSachMon.forEach((mon, index) => {
            const thanhTien = mon.gia * mon.soLuong;

            // --- LOGIC DỊCH TÊN MÓN ĂN ---
            let tenMonHienThi = mon.tenMon;
            if (currentLang === "en" && translationMap[mon.tenMon]) {
                tenMonHienThi = translationMap[mon.tenMon];
            }
            // -----------------------------

            // 🌟 Thay "HD-" + order.id thành "HD-" + displayOrderId
            html += `
            <tr>
                <td>${index === 0 ? "HD-" + displayOrderId : ""}</td>
                <td>${index + 1}</td>
                <td>${tenMonHienThi}</td>
                <td>${mon.soLuong}</td>
                <td>${formatTien(thanhTien)}</td>
                <td>${order.ngay}</td>
            </tr>
            `;
        });

        // 🌟 Thay order.id ở dòng tổng thành displayOrderId
        html += `
        <tr class="table-warning fw-bold">
            <td>
                ${totalText} HD-${displayOrderId}
            </td>
            <td colspan="3"></td>
            <td>
                ${formatTien(order.tongTien)}
            </td>
            <td></td>
        </tr>
        <tr>
            <td colspan="6" style="height:25px; border:none; background:white;"></td>
        </tr>
        `;
    });

    tbody.innerHTML = html;
};

// Gọi chạy lần đầu khi trang load xong
document.addEventListener("DOMContentLoaded", function () {
    window.loadOrderHistory();
});

function formatTien(tien) {
    return Number(tien).toLocaleString("vi-VN") + " đ";
}