// ==============================
// ORDER HISTORY - MYSQL VERSION
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
        const res = await fetch("http://localhost:8080/api/hoadon/user/" + user.id);

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
        orders.forEach((order) => {

            const displayOrderId = order.idHoaDon;
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
// FORMAT MONEY
// ==============================
function formatTien(tien, isEnglish) {
    if (isEnglish) {
        return "$" + Number(tien).toLocaleString("en-US");
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