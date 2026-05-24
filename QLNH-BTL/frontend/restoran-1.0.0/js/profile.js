// Đóng gói logic vào hàm toàn cục để file language.js có thể kích hoạt lại bất cứ lúc nào
window.loadProfile = function() {
    const userJson = localStorage.getItem("user");
    const currentLang = localStorage.getItem("currentLanguage") || "vi";

    if (!userJson) {
        // Dịch thông báo cảnh báo chưa đăng nhập
        if (currentLang === "en") {
            alert("You are not logged in yet!");
        } else {
            alert("Bạn chưa đăng nhập!");
        }
        window.location.href = "login.html";
        return;
    }

    const user = JSON.parse(userJson);

    // Gán dữ liệu Username và Email
    const usernameElem = document.getElementById("profileUsername");
    const emailElem = document.getElementById("profileEmail");
    const roleElem = document.getElementById("profileRole");

    if (usernameElem) usernameElem.innerText = user.username || "";
    if (emailElem) emailElem.innerText = user.email || "";

    // --- LOGIC DỊCH VAI TRÒ (ROLE) TỰ ĐỘNG ---
    if (roleElem) {
        const rawRole = user.vaiTro || "USER";
        
        if (currentLang === "en") {
            // Nếu là tiếng Anh
            roleElem.innerText = rawRole === "ADMIN" ? "Administrator" : "Customer";
        } else {
            // Nếu là tiếng Việt
            roleElem.innerText = rawRole === "ADMIN" ? "Quản trị viên" : "Khách hàng";
        }
    }
    // -----------------------------------------
};

// Gọi chạy lần đầu ngay khi cấu trúc trang HTML tải xong
document.addEventListener("DOMContentLoaded", function () {
    window.loadProfile();
});